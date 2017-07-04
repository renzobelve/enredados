import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { EnredadosLevelModule } from './level/level.module';
import { EnredadosQuestionModule } from './question/question.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        EnredadosLevelModule,
        EnredadosQuestionModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EnredadosEntityModule {}
