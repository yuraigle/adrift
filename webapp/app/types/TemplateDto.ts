export interface TemplateDto {
  id: number;
  questions: Question[];
  options: Record<number, string>;
}

export interface Question {
  id: number;
  name: string;
  type: 'TEXT' | 'NUMBER' | 'DECIMAL' | 'OPTION' | 'CHECKBOX';
}
