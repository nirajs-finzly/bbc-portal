import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import {
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withInterceptorsFromDi,
} from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideIcons } from '@ng-icons/core';
import {
  lucideCheck,
  lucideChevronLeft,
  lucideChevronRight,
  lucideDownload,
  lucideEllipsis,
  lucideLoader,
  lucideLogIn,
  lucideLogOut,
  lucideSend,
  lucideUser,
  lucideCreditCard,
  lucideUserPlus,
  lucideImport,
  lucideView,
  lucideFilePlus2,
  lucideEye,
  lucideTrash2,
  lucideReceipt,
  lucidePencil
} from '@ng-icons/lucide';
import { provideHotToastConfig } from '@ngxpert/hot-toast';
import { routes } from './app.routes';
import { TokenInterceptor } from './core/interceptors/token.interceptor';
import { ValidationInterceptor } from './core/interceptors/validation.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideIcons({
      lucideLogIn,
      lucideSend,
      lucideCheck,
      lucideLoader,
      lucideUser,
      lucideLogOut,
      lucideDownload,
      lucideEllipsis,
      lucideChevronLeft,
      lucideChevronRight,
      lucideCreditCard,
      lucideUserPlus,
      lucideImport,
      lucideView,
      lucideFilePlus2,
      lucideEye,
      lucideTrash2,
      lucideReceipt,
      lucidePencil,
    }),
    provideHotToastConfig({
      duration: 5000,
      position: 'bottom-right',
      autoClose: true,
      visibleToasts: 1,
    }),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimations(),
    provideHttpClient(withInterceptorsFromDi()),
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
};
