
const enum QuestionType {
    'IMAGE',
    'TEXT'

};
const enum QuestionDificulty {
    'EASY',
    'MEDIUM',
    'HARD'

};
export class Question {
    constructor(
        public id?: number,
        public question?: string,
        public type?: QuestionType,
        public dificulty?: QuestionDificulty,
        public answerTime?: number,
        public answer?: string,
    ) {
    }
}
