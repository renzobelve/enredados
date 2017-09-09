import {Player} from '../player';
import {Question} from '../question';
import {GamePlayer} from '../game-player';

const enum GameState {
    'OPEN',
    'ACTIVE',
    'CLOSE'
};

export class Game {
    constructor(
        public id?: number,
        public creatorID?: number,
        public creator?: Player,
        public state?: GameState,
        public maxPlayers?: number,
        public size?: number,
        public firstPlayer?: number,
        public activePlayer?: number,
        public activeQuestion?: number,
        public winner?: Player,
        public questions?: Question[],
        public gamePlayers?: GamePlayer[],
        public date?: Date,
    ) {

    }
}
