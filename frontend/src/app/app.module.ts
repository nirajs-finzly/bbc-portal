import {
    HTTP_INTERCEPTORS,
    provideHttpClient,
    withInterceptorsFromDi,
} from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { provideHotToastConfig } from '@ngxpert/hot-toast';
import { AppComponent } from './app.component';
import { routes } from './app.routes';
import { CoreModule } from './core/core.module';
import { ValidationInterceptor } from './core/interceptors/validation.interceptor';
import { FeaturesModule } from './features/features.module';
import { SharedModule } from './shared/shared.module';
import { TokenInterceptor } from './core/interceptors/token.interceptor';

@NgModule({
    declarations: [AppComponent],
    imports: [BrowserModule, SharedModule, CoreModule, FeaturesModule, RouterModule.forRoot(routes)],
    providers: [
        provideHttpClient(withInterceptorsFromDi()),
        provideHotToastConfig({
            duration: 5000,
            position: 'bottom-right',
            autoClose: true,
            visibleToasts: 1
        }),
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ValidationInterceptor,
            multi: true,
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: TokenInterceptor,
            multi: true,
        },
    ],
    bootstrap: [AppComponent],
})
export class AppModule {}
