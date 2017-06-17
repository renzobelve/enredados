import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { EnredadosPlayerModule } from './player/player.module';
import { EnredadosLevelModule } from './level/level.module';
import { EnredadosGamePlayerModule } from './game-player/game-player.module';
import { EnredadosGameModule } from './game/game.module';
import { EnredadosQuestionModule } from './question/question.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        EnredadosPlayerModule,
        EnredadosLevelModule,
        EnredadosGamePlayerModule,
        EnredadosGameModule,
        EnredadosQuestionModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EnredadosEntityModule {}
