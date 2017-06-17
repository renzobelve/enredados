
const enum LevelName {
    'BRONZE',
    'SILVER',
    'GOLD'

};
export class Level {
    constructor(
        public id?: number,
        public name?: LevelName,
        public minPoints?: number,
        public maxPoints?: number,
    ) {
    }
}
