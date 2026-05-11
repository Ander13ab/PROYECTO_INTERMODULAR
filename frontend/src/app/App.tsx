import { useState } from 'react';
import { AdminHomeScreen } from './components/AdminHomeScreen';
import { ClientHomeScreen } from './components/ClientHomeScreen';
import { DesktopAdminDashboard } from './components/DesktopAdminDashboard';
import { DesktopClientDashboard } from './components/DesktopClientDashboard';
import { DesktopLoginScreen } from './components/DesktopLoginScreen';
import { DesktopTrainerDashboard } from './components/DesktopTrainerDashboard';
import { LoginScreen } from './components/LoginScreen';
import { QRErrorScreen } from './components/QRErrorScreen';
import { QRProcessingScreen } from './components/QRProcessingScreen';
import { QRScannerScreen } from './components/QRScannerScreen';
import { QRSuccessClase } from './components/QRSuccessClase';
import { QRSuccessEntrada } from './components/QRSuccessEntrada';
import { QRSuccessMaquina } from './components/QRSuccessMaquina';
import { TrainerHomeScreen } from './components/TrainerHomeScreen';

type Viewport = 'mobile' | 'desktop';
type Screen =
  | 'login'
  | 'client'
  | 'trainer'
  | 'admin'
  | 'qr'
  | 'processing'
  | 'success-entry'
  | 'success-machine'
  | 'success-class'
  | 'error';

const screenOptions: Array<{ value: Screen; label: string }> = [
  { value: 'login', label: 'Login' },
  { value: 'client', label: 'Cliente' },
  { value: 'trainer', label: 'Entrenador' },
  { value: 'admin', label: 'Admin' },
  { value: 'qr', label: 'QR' },
  { value: 'processing', label: 'Procesando' },
  { value: 'success-entry', label: 'OK entrada' },
  { value: 'success-machine', label: 'OK maquina' },
  { value: 'success-class', label: 'OK clase' },
  { value: 'error', label: 'Error' },
];

function FilterButton({
  active,
  label,
  onClick,
}: {
  active: boolean;
  label: string;
  onClick: () => void;
}) {
  return (
    <button
      className={`rounded-full px-4 py-2 text-sm transition ${
        active ? 'bg-[#0D0D14] text-white' : 'bg-[#EEF1F5] text-[#667085]'
      }`}
      onClick={onClick}
    >
      {label}
    </button>
  );
}

function ScreenButton({
  active,
  label,
  onClick,
}: {
  active: boolean;
  label: string;
  onClick: () => void;
}) {
  return (
    <button
      className={`rounded-full px-4 py-2 text-sm transition ${
        active ? 'bg-[#FF4D2E] text-white' : 'bg-[#EEF1F5] text-[#667085]'
      }`}
      onClick={onClick}
    >
      {label}
    </button>
  );
}

export default function App() {
  const [viewport, setViewport] = useState<Viewport>('desktop');
  const [screen, setScreen] = useState<Screen>('login');

  const renderPreview = () => {
    if (viewport === 'desktop') {
      switch (screen) {
        case 'client':
          return <DesktopClientDashboard />;
        case 'trainer':
          return <DesktopTrainerDashboard />;
        case 'admin':
          return <DesktopAdminDashboard />;
        default:
          return <DesktopLoginScreen />;
      }
    }

    switch (screen) {
      case 'client':
        return <ClientHomeScreen />;
      case 'trainer':
        return <TrainerHomeScreen />;
      case 'admin':
        return <AdminHomeScreen />;
      case 'qr':
        return <QRScannerScreen />;
      case 'processing':
        return <QRProcessingScreen />;
      case 'success-entry':
        return <QRSuccessEntrada />;
      case 'success-machine':
        return <QRSuccessMaquina />;
      case 'success-class':
        return <QRSuccessClase />;
      case 'error':
        return <QRErrorScreen />;
      default:
        return <LoginScreen />;
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
                Base visual para movil y escritorio
              </h1>
              <p className="mt-4 text-base leading-7 text-[#667085]">
                Punto de partida real para convertir los prototipos de Figma en
                la app conectada al backend. Aqui ya quedan visibles las
                pantallas principales y las primeras versiones desktop.
              </p>
            </div>

            <div className="grid gap-4 md:grid-cols-2">
              <div>
                <p className="mb-2 text-xs font-bold uppercase tracking-[0.22em] text-[#98A2B3]">
                  Formato
                </p>
                <div className="inline-flex rounded-full bg-[#EEF1F5] p-1">
                  <FilterButton
                    active={viewport === 'desktop'}
                    label="Escritorio"
                    onClick={() => setViewport('desktop')}
                  />
                  <FilterButton
                    active={viewport === 'mobile'}
                    label="Movil"
                    onClick={() => setViewport('mobile')}
                  />
                </div>
              </div>

              <div>
                <p className="mb-2 text-xs font-bold uppercase tracking-[0.22em] text-[#98A2B3]">
                  Pantalla
                </p>
                <div className="flex max-w-[560px] flex-wrap gap-2">
                  {screenOptions.map((option) => (
                    <ScreenButton
                      key={option.value}
                      active={screen === option.value}
                      label={option.label}
                      onClick={() => setScreen(option.value)}
                    />
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="mb-8 flex flex-wrap gap-3 text-sm">
          <div className="rounded-full bg-white px-4 py-2 text-[#667085] shadow-sm">
            Backend listo: auth, usuarios, maquinas, clases, rutinas, QR y cuotas
          </div>
          <div className="rounded-full bg-white px-4 py-2 text-[#667085] shadow-sm">
            Siguiente integracion natural: `/api/auth/login`, `/api/auth/me` y dashboard cliente
          </div>
        </div>

        {renderPreview()}
      </div>
    </div>
  );
}
