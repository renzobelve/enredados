
const enum GameState {
    'OPEN',
    'ACTIVE',
    'CLOSE'

};
import { GamePlayer } from '../game-player';
import { Question } from '../question';
export class Game {
    constructor(
        public id?: number,
        public state?: GameState,
        public maxPlayers?: number,
        public activePlayer?: number,
        public activeQuestion?: number,
        public gamePlayers?: GamePlayer,
        public questions?: Question,
    ) {
    }
}
