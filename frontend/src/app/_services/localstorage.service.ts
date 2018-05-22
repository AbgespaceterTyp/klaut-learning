import { Injectable } from '@angular/core';

@Injectable()
export class LocalStorageService {
    private static readonly CURRENT_ORGANIZATION = 'currentOrganization';
    private static readonly CURRENT_USER = 'currentUser';

    get currentOrganization(): string {
        return localStorage.getItem(LocalStorageService.CURRENT_ORGANIZATION)
    }

    set currentOrganization(organization: string) {
        localStorage.setItem(LocalStorageService.CURRENT_ORGANIZATION, organization);
    }

    removeCurrentOrganization() {
        localStorage.removeItem(LocalStorageService.CURRENT_ORGANIZATION);
    }

    removeCurrentUser() {
        localStorage.removeItem(LocalStorageService.CURRENT_USER)
    }

    get currentUser(): string {
        return localStorage.getItem(LocalStorageService.CURRENT_USER)
    }

    set currentUser(email: string) {
        localStorage.setItem(LocalStorageService.CURRENT_USER, email);
    }

}