import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {EnredadosSharedModule} from '../../shared';
import {
    GameService,
    GameComponent,
    GameNewComponent,
    GameWaitingComponent,
    gameRoute,
    GameResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...gameRoute,
];

@NgModule({
    imports: [
        EnredadosSharedModule,
        RouterModule.forRoot(ENTITY_STATES, {useHash: true})
    ],
    declarations: [
        GameComponent,
        GameNewComponent,
        GameWaitingComponent,
    ],
    entryComponents: [
        GameComponent,
        GameNewComponent,
        GameWaitingComponent,
    ],
    providers: [
        GameService,
        GameResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EnredadosGameModule {}
