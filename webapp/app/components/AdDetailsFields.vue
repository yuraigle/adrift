<script setup lang="ts">
const props = defineProps<{
  fields: FieldDto[],
  template: TemplateDto
}>()

const getQ = (qid: number):QuestionDto | null => {
  return props.template.questions.find(q => q.id === qid) || null
}

const oName = (oid: string):string => {
  const n = Number.parseInt(oid)
  return props.template.options[n] || ''
}
</script>

<template>
  <h3 class="text-xl font-semibold mb-2">Features</h3>
  <div v-for="field in fields" :key="field.qid" class="flex gap-x-2">
    <div class="w-1/3">
      {{ getQ(field.qid)?.name || '' }}:
    </div>
    <div>
      <template v-if="getQ(field.qid)?.type === 'OPTION'">
        {{ oName(field.value) }}
      </template>
      <template v-else-if="getQ(field.qid)?.type === 'CHECKBOX'">
        <template v-for="oid in field.value.split(',')" :key="oid">
          {{ oName(oid) }}<br>
        </template>
      </template>
      <template v-else>
        {{ field.value }}
      </template>
    </div>
  </div>
</template>
