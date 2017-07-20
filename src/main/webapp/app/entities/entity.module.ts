import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { EnredadosLevelModule } from './level/level.module';
import { EnredadosQuestionModule } from './question/question.module';
import { EnredadosGameModule } from './game/game.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        EnredadosLevelModule,
        EnredadosQuestionModule,
        EnredadosGameModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EnredadosEntityModule {}
