import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EnredadosSharedModule } from '../../shared';
import { EnredadosAdminModule } from '../../admin/admin.module';
import {
    PlayerService,
    PlayerPopupService,
    PlayerComponent,
    PlayerDetailComponent,
    PlayerDialogComponent,
    PlayerPopupComponent,
    PlayerDeletePopupComponent,
    PlayerDeleteDialogComponent,
    playerRoute,
    playerPopupRoute,
} from './';

const ENTITY_STATES = [
    ...playerRoute,
    ...playerPopupRoute,
];

@NgModule({
    imports: [
        EnredadosSharedModule,
        EnredadosAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PlayerComponent,
        PlayerDetailComponent,
        PlayerDialogComponent,
        PlayerDeleteDialogComponent,
        PlayerPopupComponent,
        PlayerDeletePopupComponent,
    ],
    entryComponents: [
        PlayerComponent,
        PlayerDialogComponent,
        PlayerPopupComponent,
        PlayerDeleteDialogComponent,
        PlayerDeletePopupComponent,
    ],
    providers: [
        PlayerService,
        PlayerPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EnredadosPlayerModule {}
