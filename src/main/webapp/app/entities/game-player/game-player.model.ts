import {Player} from '../player';
export class GamePlayer {
    constructor(
        public id?: number,
        public points?: number,
        public correctQuestions?: number,
        public player?: Player,
    ) {

    }
}
