export interface QuestionDto {
  id: number;
  name: string;
  type: 'TEXT' | 'NUMBER' | 'DECIMAL' | 'OPTION' | 'CHECKBOX';
}
