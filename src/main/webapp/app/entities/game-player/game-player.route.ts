import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { GamePlayerComponent } from './game-player.component';
import { GamePlayerDetailComponent } from './game-player-detail.component';
import { GamePlayerPopupComponent } from './game-player-dialog.component';
import { GamePlayerDeletePopupComponent } from './game-player-delete-dialog.component';

import { Principal } from '../../shared';

export const gamePlayerRoute: Routes = [
    {
        path: 'game-player',
        component: GamePlayerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GamePlayers'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'game-player/:id',
        component: GamePlayerDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GamePlayers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const gamePlayerPopupRoute: Routes = [
    {
        path: 'game-player-new',
        component: GamePlayerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GamePlayers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'game-player/:id/edit',
        component: GamePlayerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GamePlayers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'game-player/:id/delete',
        component: GamePlayerDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GamePlayers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
