import { useEffect, useState } from 'react';
import { DesktopAdminDashboard } from './components/DesktopAdminDashboard';
import { DesktopClientDashboard } from './components/DesktopClientDashboard';
import { DesktopLoginScreen } from './components/DesktopLoginScreen';
import { DesktopTrainerDashboard } from './components/DesktopTrainerDashboard';
import {
  clearSessionToken,
  getAuthenticatedUser,
  login,
  readSessionToken,
  saveSessionToken,
} from './services/authService';
import type { AuthenticatedUser } from './types/auth';

type SessionStatus = 'checking' | 'logged_out' | 'logged_in';

export default function App() {
  const [status, setStatus] = useState<SessionStatus>('checking');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [user, setUser] = useState<AuthenticatedUser | null>(null);

  useEffect(() => {
    const restoreSession = async () => {
      const token = readSessionToken();

      if (!token) {
        setStatus('logged_out');
        return;
      }

      try {
        const authenticatedUser = await getAuthenticatedUser(token);
        setUser(authenticatedUser);
        setStatus('logged_in');
      } catch {
        clearSessionToken();
        setStatus('logged_out');
      }
    };

    void restoreSession();
  }, []);

  const handleLogin = async () => {
    if (!email.trim() || !password.trim()) {
      setErrorMessage('Completa el email y la contrasena para continuar.');
      return;
    }

    try {
      setIsSubmitting(true);
      setErrorMessage(null);

      const authResponse = await login({
        email: email.trim(),
        password,
      });

      saveSessionToken(authResponse.token);

      const authenticatedUser = await getAuthenticatedUser(authResponse.token);
      setUser(authenticatedUser);
      setStatus('logged_in');
      setPassword('');
    } catch (error) {
      setErrorMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido iniciar sesion.',
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleLogout = () => {
    clearSessionToken();
    setUser(null);
    setPassword('');
    setStatus('logged_out');
  };

  const renderDashboard = () => {
    if (!user) {
      return null;
    }

    switch (user.role) {
      case 'ADMIN':
        return (
          <DesktopAdminDashboard
            onLogout={handleLogout}
            userName={user.nombre}
          />
        );
      case 'TRAINER':
        return (
          <DesktopTrainerDashboard
            onLogout={handleLogout}
            userId={user.id}
            userName={user.nombre}
          />
        );
      case 'CLIENT':
      default:
        return (
          <DesktopClientDashboard
            onLogout={handleLogout}
            userId={user.id}
            userName={user.nombre}
          />
        );
    }
  };

  const content =
    status === 'checking' ? (
      <div className="flex min-h-screen items-center justify-center bg-[radial-gradient(circle_at_top_left,_rgba(255,92,61,0.16),_transparent_22%),radial-gradient(circle_at_top_right,_rgba(34,102,255,0.14),_transparent_24%),linear-gradient(180deg,#0D0D14_0%,#121523_100%)] px-6 py-12">
        <div className="w-full max-w-[560px] rounded-[32px] border border-white/40 bg-white/80 p-12 text-center shadow-[0_28px_90px_rgba(15,23,42,0.12)] backdrop-blur">
          <div className="mx-auto mb-6 flex h-16 w-16 items-center justify-center rounded-2xl bg-[linear-gradient(135deg,#FF5C3D_0%,#FF8A66_100%)] font-['Syne'] text-2xl font-extrabold text-white">
            HG
          </div>
          <p className="text-sm font-semibold uppercase tracking-[0.28em] text-[#98A2B3]">
            Hazel Gym
          </p>
          <h2 className="mt-4 font-['Syne'] text-4xl font-extrabold text-[#0D0D14]">
            Comprobando tu sesion
          </h2>
          <p className="mt-4 text-base leading-7 text-[#667085]">
            Estamos preparando tu acceso para llevarte directamente a tu panel.
          </p>
        </div>
      </div>
    ) : status === 'logged_in' ? (
      renderDashboard()
    ) : (
      <DesktopLoginScreen
        email={email}
        errorMessage={errorMessage}
        isLoading={isSubmitting}
        password={password}
        onEmailChange={setEmail}
        onPasswordChange={setPassword}
        onSubmit={handleLogin}
      />
    );

  return (
    <div className="min-h-screen bg-[#0D0D14]">
      {content}
    </div>
  );
}
