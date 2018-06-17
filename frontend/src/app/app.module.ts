import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { RegisterComponent } from './pages/register/register.component';
import { ImpressumComponent } from './pages/impressum/impressum.component';

import { AuthenticationService, ModelService, ErrorHttpInterceptor, LocalStorageService, OrganizationService, MessageService } from './_services/index';
import { routing } from './app.routing';
import { AccordionComponent } from './components/accordion/accordion-body/accordion.component';
import { AccordionItemComponent } from './components/accordion/accordion-item/accordion-item.component';
import { UserComponent } from './components/user/user.component';
import { UserService } from './_services/user.service';
import { LoadingComponent } from './components/loading/loading.component';
import { CookieLawModule } from 'angular2-cookie-law';
import { ToastrModule } from 'ngx-toastr';

import { FooterComponent } from './components/footer/footer.component';
import { OrganizationComponent } from './pages/organization/organization.component';
import { ModelTestingComponent } from './components/model-testing/model-testing.component';
import 'moment-duration-format';
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    RegisterComponent,
    AccordionComponent,
    AccordionItemComponent,
    UserComponent,
    LoadingComponent,
    FooterComponent,
    ImpressumComponent,
    OrganizationComponent,
    ModelTestingComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    routing,
    BrowserAnimationsModule,
    CookieLawModule,
    BrowserAnimationsModule, // required animations module
    ToastrModule.forRoot()
  ],
  providers: [
    AuthenticationService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHttpInterceptor,
      multi: true
    },
    MessageService,
    ModelService,
    UserService,
    OrganizationService,
    LocalStorageService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
