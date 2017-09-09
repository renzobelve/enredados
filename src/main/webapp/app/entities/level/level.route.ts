import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { LevelComponent } from './level.component';
import { LevelDetailComponent } from './level-detail.component';
import { LevelPopupComponent } from './level-dialog.component';
import { LevelDeletePopupComponent } from './level-delete-dialog.component';

import { Principal } from '../../shared';

export const levelRoute: Routes = [
    {
        path: 'level',
        component: LevelComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Levels'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'level/:id',
        component: LevelDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Levels'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const levelPopupRoute: Routes = [
    {
        path: 'level-new',
        component: LevelPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Levels'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'level/:id/edit',
        component: LevelPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Levels'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'level/:id/delete',
        component: LevelDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Levels'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
