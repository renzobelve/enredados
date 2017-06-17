import { User } from '../../shared';
import { GamePlayer } from '../game-player';
import { Level } from '../level';
export class Player {
    constructor(
        public id?: number,
        public points?: number,
        public user?: User,
        public gamePlayers?: GamePlayer,
        public level?: Level,
    ) {
    }
}
