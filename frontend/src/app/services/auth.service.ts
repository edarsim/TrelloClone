import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, map, Observable, throwError } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
private baseUrl = 'http://localhost:8080/api/auth';
private tokenKey = 'authToken';

  constructor(private http: HttpClient, private router: Router) { }

    getToken(): string | null {
      return localStorage.getItem(this.tokenKey);
    }

    login(username: string, password: string): Observable<any> {
      const payload = { username, password }
      return this.http.post<{ token: string }>(`${this.baseUrl}/login`, payload).pipe(
        map((response: { token: string}) => {
          const token = response.token; // Extract the token from JwtResponse
          this.saveToken(token); // Store the token in local storage
        }),
        catchError(error => {
          console.error('Login failed:', error);
          return throwError(() => error);
        })
      );
    }

    saveToken(token: string): void{
      localStorage.setItem(this.tokenKey, token);
    }

    logout(): void {
      localStorage.removeItem(this.tokenKey);
      this.router.navigate(['/login']);
    }

    isLoggedIn(): boolean{
      return !!localStorage.getItem(this.tokenKey);
    }

    register(username: string, password: string, email: string): Observable<any> {
      return this.http.post<{ token: string }>(`${this.baseUrl}/register`, {username, password, email});
    }

    getUsername(): string | null {
      const token = this.getToken();
      if(!token){
        console.warn('No token found in local storage');
        return null;
      }

      try {
        const decoded: any = jwtDecode<any>(token);
        return decoded?.sub || null;
      } catch (error) {
        console.error('Error decoding token', error);
        return null;
      }
    }
  
}

