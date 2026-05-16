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
            userName={user.nombre}
          />
        );
      case 'CLIENT':
      default:
        return (
          <DesktopClientDashboard
            onLogout={handleLogout}
            userName={user.nombre}
          />
        );
    }
  };

  return (
    <div className="min-h-screen bg-[linear-gradient(180deg,#F7F8FA_0%,#EEF1F5_100%)] p-6 md:p-8">
      <div className="mx-auto max-w-[1600px]">
        <div className="mb-8 rounded-[28px] border border-white/70 bg-white/85 p-6 shadow-[0_24px_80px_rgba(15,23,42,0.08)] backdrop-blur">
          <div className="flex flex-col gap-6 xl:flex-row xl:items-end xl:justify-between">
            <div className="max-w-3xl">
              <p className="text-sm font-semibold uppercase tracking-[0.24em] text-[#667085]">
                Hazel Gym Frontend
              </p>
              <h1 className="mt-3 font-['Syne'] text-4xl font-extrabold text-[#0D0D14] md:text-5xl">
                Aplicacion web sencilla conectada al backend
              </h1>
              <p className="mt-4 text-base leading-7 text-[#667085]">
                Esta version web esta pensada para ser facil de entender:
                login real, sesion simple y panel de escritorio distinto segun
                el rol autenticado.
              </p>
            </div>
          </div>
        </div>

        <div className="mb-8 flex flex-wrap gap-3 text-sm">
          <div className="rounded-full bg-white px-4 py-2 text-[#667085] shadow-sm">
            Paso actual: login real con `/api/auth/login` y lectura del usuario
            con `/api/auth/me`
          </div>
          <div className="rounded-full bg-white px-4 py-2 text-[#667085] shadow-sm">
            Siguiente integracion natural: empezar a conectar datos reales
            dentro de cada panel
          </div>
        </div>

        {status === 'checking' ? (
          <div className="rounded-[32px] border border-white/70 bg-white p-12 text-center shadow-[0_24px_80px_rgba(15,23,42,0.08)]">
            <p className="text-sm font-semibold uppercase tracking-[0.24em] text-[#98A2B3]">
              Hazel Gym
            </p>
            <h2 className="mt-4 font-['Syne'] text-4xl font-extrabold text-[#0D0D14]">
              Comprobando la sesion actual
            </h2>
            <p className="mt-4 text-base text-[#667085]">
              Estamos revisando si ya habia una sesion abierta en este navegador.
            </p>
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
        )}
      </div>
    </div>
  );
}
