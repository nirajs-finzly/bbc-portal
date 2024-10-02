import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { Toast, ToastType } from '../../types/toast';

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  private activeToast: Toast | null = null;
  private toastSubject: Subject<Toast | null> = new Subject<Toast | null>();
  private autoIncrementId = 0;

  constructor() {}

  getToast(): Observable<Toast | null> {
    return this.toastSubject.asObservable();
  }

  show(
    message: string,
    type: ToastType = 'info',
    duration: number = 3000
  ): void {
    const toast: Toast = {
      id: this.autoIncrementId++,
      message,
      type,
      duration,
    };

    // Replace the existing toast with the new one
    this.activeToast = toast;
    this.toastSubject.next(this.activeToast);

    if (duration !== 0) {
      setTimeout(() => this.removeToast(), duration);
    }
  }

  success(message: string, duration?: number): void {
    this.show(message, 'success', duration);
  }

  error(message: string, duration?: number): void {
    this.show(message, 'error', duration);
  }

  info(message: string, duration?: number): void {
    this.show(message, 'info', duration);
  }

  warning(message: string, duration?: number): void {
    this.show(message, 'warning', duration);
  }

  removeToast(): void {
    this.activeToast = null;
    this.toastSubject.next(this.activeToast);
  }
}