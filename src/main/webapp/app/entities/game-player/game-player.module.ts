import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EnredadosSharedModule } from '../../shared';
import {
    GamePlayerService,
    GamePlayerPopupService,
    GamePlayerComponent,
    GamePlayerDetailComponent,
    GamePlayerDialogComponent,
    GamePlayerPopupComponent,
    GamePlayerDeletePopupComponent,
    GamePlayerDeleteDialogComponent,
    gamePlayerRoute,
    gamePlayerPopupRoute,
} from './';

const ENTITY_STATES = [
    ...gamePlayerRoute,
    ...gamePlayerPopupRoute,
];

@NgModule({
    imports: [
        EnredadosSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        GamePlayerComponent,
        GamePlayerDetailComponent,
        GamePlayerDialogComponent,
        GamePlayerDeleteDialogComponent,
        GamePlayerPopupComponent,
        GamePlayerDeletePopupComponent,
    ],
    entryComponents: [
        GamePlayerComponent,
        GamePlayerDialogComponent,
        GamePlayerPopupComponent,
        GamePlayerDeleteDialogComponent,
        GamePlayerDeletePopupComponent,
    ],
    providers: [
        GamePlayerService,
        GamePlayerPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EnredadosGamePlayerModule {}
