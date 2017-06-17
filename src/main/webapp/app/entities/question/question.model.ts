
const enum QuestionType {
    'IMAGE',
    'TEXT'

};
import { Game } from '../game';
export class Question {
    constructor(
        public id?: number,
        public question?: string,
        public type?: QuestionType,
        public answerTime?: number,
        public answer?: string,
        public games?: Game,
    ) {
    }
}
