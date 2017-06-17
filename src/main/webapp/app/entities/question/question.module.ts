import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EnredadosSharedModule } from '../../shared';
import {
    QuestionService,
    QuestionPopupService,
    QuestionComponent,
    QuestionDetailComponent,
    QuestionDialogComponent,
    QuestionPopupComponent,
    QuestionDeletePopupComponent,
    QuestionDeleteDialogComponent,
    questionRoute,
    questionPopupRoute,
    QuestionResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...questionRoute,
    ...questionPopupRoute,
];

@NgModule({
    imports: [
        EnredadosSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        QuestionComponent,
        QuestionDetailComponent,
        QuestionDialogComponent,
        QuestionDeleteDialogComponent,
        QuestionPopupComponent,
        QuestionDeletePopupComponent,
    ],
    entryComponents: [
        QuestionComponent,
        QuestionDialogComponent,
        QuestionPopupComponent,
        QuestionDeleteDialogComponent,
        QuestionDeletePopupComponent,
    ],
    providers: [
        QuestionService,
        QuestionPopupService,
        QuestionResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EnredadosQuestionModule {}
