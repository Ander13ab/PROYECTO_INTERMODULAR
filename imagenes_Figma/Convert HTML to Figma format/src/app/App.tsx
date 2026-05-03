import { LoginScreen } from './components/LoginScreen';
import { ClientHomeScreen } from './components/ClientHomeScreen';
import { TrainerHomeScreen } from './components/TrainerHomeScreen';
import { AdminHomeScreen } from './components/AdminHomeScreen';
import { QRScannerScreen } from './components/QRScannerScreen';
import { QRProcessingScreen } from './components/QRProcessingScreen';
import { QRSuccessEntrada } from './components/QRSuccessEntrada';
import { QRSuccessMaquina } from './components/QRSuccessMaquina';
import { QRSuccessClase } from './components/QRSuccessClase';
import { QRErrorScreen } from './components/QRErrorScreen';

export default function App() {
  return (
    <div className="min-h-screen bg-[#F5F4F0] p-8">
      <div className="max-w-7xl mx-auto">
        <h1 className="font-['Syne'] text-3xl font-bold mb-8 text-[#0D0D14]">
          Hazel Gym - Diseño de Pantallas
        </h1>

        {/* Login & Onboarding */}
        <section className="mb-12">
          <h2 className="font-['Syne'] text-xs font-bold uppercase tracking-wider text-[#00000055] mb-4">
            Login & Onboarding
          </h2>
          <div className="flex gap-5 flex-wrap">
            <div>
              <LoginScreen />
              <p className="text-xs text-center text-[#00000055] mt-2">Login</p>
            </div>
            <div>
              <ClientHomeScreen />
              <p className="text-xs text-center text-[#00000055] mt-2">Home — Cliente</p>
            </div>
          </div>
        </section>

        {/* Roles Profesionales */}
        <section className="mb-12">
          <h2 className="font-['Syne'] text-xs font-bold uppercase tracking-wider text-[#00000055] mb-4">
            Roles Profesionales
          </h2>
          <div className="flex gap-5 flex-wrap">
            <div>
              <TrainerHomeScreen />
              <p className="text-xs text-center text-[#00000055] mt-2">Home — Entrenador</p>
            </div>
            <div>
              <AdminHomeScreen />
              <p className="text-xs text-center text-[#00000055] mt-2">Home — Admin</p>
            </div>
            <div>
              <QRScannerScreen />
              <p className="text-xs text-center text-[#00000055] mt-2">Escáner QR (Cliente)</p>
            </div>
          </div>
        </section>

        {/* Flujo QR */}
        <section className="mb-12">
          <h2 className="font-['Syne'] text-xs font-bold uppercase tracking-wider text-[#00000055] mb-4">
            Flujo Principal — Escaneo QR
          </h2>
          <div className="flex items-center gap-4 mb-6 text-xs text-[#00000066]">
            <div className="flex items-center gap-2">
              <div className="w-2 h-2 rounded-full bg-[#FF4D2E]"></div>
              Cliente — entrada
            </div>
            <div className="flex items-center gap-2">
              <div className="w-2 h-2 rounded-full bg-[#2266FF]"></div>
              Cliente — máquina
            </div>
            <div className="flex items-center gap-2">
              <div className="w-2 h-2 rounded-full bg-[#2266FF]"></div>
              Entrenador — clase
            </div>
            <div className="flex items-center gap-2">
              <div className="w-2 h-2 rounded-full bg-[#E24B4A]"></div>
              Error
            </div>
          </div>
          <div className="flex gap-5 flex-wrap items-start">
            <div>
              <QRScannerScreen variant="flow" />
              <p className="text-xs text-center text-[#00000055] mt-2 max-w-[152px] leading-tight">
                1. Escáner activo<br />
                <span className="text-[#FF4D2E]">tab: Entrada</span>
              </p>
            </div>
            <div className="text-2xl text-[#00000055] self-center -mt-4">→</div>
            <div>
              <QRProcessingScreen />
              <p className="text-xs text-center text-[#00000055] mt-2 max-w-[152px] leading-tight">
                2. Procesando<br />
                <span className="text-[#00000066]">validación servidor</span>
              </p>
            </div>
            <div className="text-2xl text-[#00000055] self-center -mt-4">→</div>
            <div>
              <QRSuccessEntrada />
              <p className="text-xs text-center text-[#00000055] mt-2 max-w-[152px] leading-tight">
                3a. Éxito — Entrada<br />
                <span className="text-[#22CC66]">cliente</span>
              </p>
            </div>
          </div>
        </section>

        {/* Resultados Alternativos */}
        <section className="mb-12">
          <h2 className="font-['Syne'] text-xs font-bold uppercase tracking-wider text-[#00000055] mb-4">
            Resultados Alternativos — Tipo Máquina y Clase
          </h2>
          <div className="flex gap-5 flex-wrap items-start">
            <div>
              <QRSuccessMaquina />
              <p className="text-xs text-center text-[#00000055] mt-2 max-w-[152px] leading-tight">
                3b. QR Máquina<br />
                <span className="text-[#2266FF]">ficha técnica</span>
              </p>
            </div>
            <div>
              <QRSuccessClase />
              <p className="text-xs text-center text-[#00000055] mt-2 max-w-[152px] leading-tight">
                3c. QR Clase<br />
                <span className="text-[#2266FF]">entrenador</span>
              </p>
            </div>
            <div className="text-sm text-[#00000055] self-center -mt-4">o bien</div>
            <div>
              <QRErrorScreen />
              <p className="text-xs text-center text-[#00000055] mt-2 max-w-[152px] leading-tight">
                4. Error<br />
                <span className="text-[#E24B4A]">QR inválido o expirado</span>
              </p>
            </div>
          </div>
        </section>
      </div>
    </div>
  );
}
