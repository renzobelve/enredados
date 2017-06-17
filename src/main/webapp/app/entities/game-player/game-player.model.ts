import { Player } from '../player';
import { Game } from '../game';
export class GamePlayer {
    constructor(
        public id?: number,
        public correctQuestions?: number,
        public points?: number,
        public player?: Player,
        public game?: Game,
    ) {
    }
}
