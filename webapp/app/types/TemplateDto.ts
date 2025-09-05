export interface TemplateDto {
  id: number;
  questions: QuestionDto[];
  options: Record<number, string>;
}

export interface QuestionDto {
  id: number;
  name: string;
  type: 'TEXT' | 'NUMBER' | 'DECIMAL' | 'OPTION' | 'CHECKBOX';
}
