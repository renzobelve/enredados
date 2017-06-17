import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { PlayerComponent } from './player.component';
import { PlayerDetailComponent } from './player-detail.component';
import { PlayerPopupComponent } from './player-dialog.component';
import { PlayerDeletePopupComponent } from './player-delete-dialog.component';

import { Principal } from '../../shared';

export const playerRoute: Routes = [
    {
        path: 'player',
        component: PlayerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Players'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'player/:id',
        component: PlayerDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Players'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const playerPopupRoute: Routes = [
    {
        path: 'player-new',
        component: PlayerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Players'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'player/:id/edit',
        component: PlayerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Players'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'player/:id/delete',
        component: PlayerDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Players'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
