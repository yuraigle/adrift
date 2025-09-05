export interface TemplateDto {
  id: number;
  questions: QuestionDto[];
  options: Record<number, string>;
}
