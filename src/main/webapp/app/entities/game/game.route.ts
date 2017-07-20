import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {PaginationUtil} from 'ng-jhipster';

import {GameComponent} from './game.component';
import {GameNewComponent} from './game-new.component';
import {GameWaitingComponent} from './game-waiting.component';

import {Principal} from '../../shared';

@Injectable()
export class GameResolvePagingParams implements Resolve<any> {

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

export const gameRoute: Routes = [
    {
        path: 'game-new',
        component: GameNewComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Games'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'game-waiting/:id',
        component: GameWaitingComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Games'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'game',
        component: GameComponent,
        resolve: {
            'pagingParams': GameResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Games'
        },
        canActivate: [UserRouteAccessService]
    }
];
