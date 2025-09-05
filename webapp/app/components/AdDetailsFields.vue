<script setup lang="ts">
import type { FieldDto } from '~/types/AdDetailsDto';
import type { TemplateDto, QuestionDto } from '~/types/TemplateDto';

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
  <table v-if="fields">
    <tr v-for="field in fields" :key="field.qid">
      <td class="pe-4 align-top">
        <template v-if="getQ(field.qid)">
          {{ getQ(field.qid)?.name || '' }}:
        </template>
      </td>
      <td>
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
      </td>
    </tr>
  </table>
</template>
