
const enum QuestionType {
    'IMAGE',
    'TEXT'

};
export class Question {
    constructor(
        public id?: number,
        public question?: string,
        public type?: QuestionType,
        public answerTime?: number,
        public answer?: string,
    ) {
    }
}
