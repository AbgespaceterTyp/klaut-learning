import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './register/register.component';

import { AuthenticationService, ModelService, OrganizationService, ErrorHttpInterceptor, LocalStorageService } from './_services/index';
import { routing } from './app.routing';
import { AccordionComponent } from './components/accordion/accordion-body/accordion.component';
import { AccordionItemComponent } from './components/accordion/accordion-item/accordion-item.component';
import { OrganizationListComponent } from './organization-list/organization-list.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    RegisterComponent,
    AccordionComponent,
    AccordionItemComponent,
    OrganizationListComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    routing
  ],
  providers: [
    AuthenticationService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHttpInterceptor,
      multi: true
    },
    ModelService,
    OrganizationService,
    LocalStorageService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
