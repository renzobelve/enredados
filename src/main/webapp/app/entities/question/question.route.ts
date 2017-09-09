import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { QuestionComponent } from './question.component';
import { QuestionPopupComponent } from './question-dialog.component';
import { QuestionDeletePopupComponent } from './question-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class QuestionResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: PaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const questionRoute: Routes = [
    {
        path: 'question',
        component: QuestionComponent,
        resolve: {
            'pagingParams': QuestionResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Questions'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const questionPopupRoute: Routes = [
    {
        path: 'question-new',
        component: QuestionPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Questions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'question/:id/edit',
        component: QuestionPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Questions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'question/:id/delete',
        component: QuestionDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Questions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
