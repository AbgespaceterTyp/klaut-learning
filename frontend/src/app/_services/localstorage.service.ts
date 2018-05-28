import { Injectable } from '@angular/core';
import { OrganizationDto } from '../_models';

@Injectable()
export class LocalStorageService {
    private static readonly ORG_KEY = 'ORG_KEY';
    private static readonly ORG_NAME = 'ORG_NAME';
    private static readonly ORG_ICON = 'ORG_ICON';
    private static readonly CURRENT_USER = 'currentUser';

    get currentOrganization(): OrganizationDto {
        let name = localStorage[LocalStorageService.ORG_NAME]
        let key = localStorage[LocalStorageService.ORG_KEY]
        let iconUrl = localStorage[LocalStorageService.ORG_ICON]
        return new OrganizationDto(name, key, iconUrl)
    }

    set currentOrganization(organization: OrganizationDto) {
        localStorage[LocalStorageService.ORG_KEY] = organization.key;
        localStorage[LocalStorageService.ORG_NAME] = organization.name;
        localStorage[LocalStorageService.ORG_ICON] = organization.iconUrl;
    }

    removeCurrentOrganization() {
        localStorage.removeItem(LocalStorageService.ORG_ICON);
        localStorage.removeItem(LocalStorageService.ORG_NAME);
        localStorage.removeItem(LocalStorageService.ORG_KEY);
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