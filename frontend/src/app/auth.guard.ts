import { Injectable } from '@angular/core';
import { CanActivate, CanActivateFn, Router } from '@angular/router';
import { AuthService } from './services/auth.service';


@Injectable({
  'providedIn': 'root',
})

export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (this.authService.isLoggedIn()) {
      return true;
    }
    console.error("You are not authorized");
    this.router.navigate(['/login']); // Redirect to login if not authenticated
    return false;
  }
}
