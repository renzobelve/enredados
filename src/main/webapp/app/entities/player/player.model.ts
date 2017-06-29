import {User} from '../../shared';
import {Level} from '../level';
export class Player {
    constructor(
        public id?: number,
        public points?: number,
        public user?: User,
        public level?: Level,
    ) {

    }
}
