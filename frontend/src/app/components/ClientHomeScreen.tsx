import { PhoneFrame } from './PhoneFrame';

export function ClientHomeScreen() {
  return (
    <PhoneFrame statusBarColor="#00000066">
      <div className="bg-[#F5F4F0] min-h-[360px]">
        <div className="bg-[#0D0D14] px-4 pt-3.5 pb-[18px] rounded-b-[20px]">
          <div className="text-[10px] text-[#ffffff55] mb-1">Hola de nuevo</div>
          <div className="font-['Syne'] text-base font-extrabold text-white">Carlos M.</div>
          <div className="inline-flex items-center gap-1 bg-[#FF4D2E] rounded-full px-2 py-1 mt-2">
            <div className="w-[5px] h-[5px] bg-[#FFD0C5] rounded-full"></div>
            <span className="text-[9px] text-white font-medium">14 días seguidos</span>
          </div>
        </div>

        <div className="px-3.5 pt-3.5">
          <div className="flex gap-2 mb-2.5">
            <div className="flex-1 bg-white rounded-xl px-2.5 py-2.5 border-[0.5px] border-[#0000000f]">
              <div className="font-['Syne'] text-[20px] font-extrabold text-[#0D0D14]">47</div>
              <div className="text-[9px] text-[#00000055]">Visitas totales</div>
            </div>
            <div className="flex-1 bg-white rounded-xl px-2.5 py-2.5 border-[0.5px] border-[#0000000f]">
              <div className="font-['Syne'] text-[20px] font-extrabold text-[#0D0D14]">14</div>
              <div className="text-[9px] text-[#00000055]">Racha actual</div>
            </div>
            <div className="flex-1 bg-white rounded-xl px-2.5 py-2.5 border-[0.5px] border-[#0000000f]">
              <div className="font-['Syne'] text-[20px] font-extrabold text-[#0D0D14]">3</div>
              <div className="text-[9px] text-[#00000055]">Logros</div>
            </div>
          </div>

          <div className="text-[9px] font-bold uppercase tracking-wider text-[#00000055] mb-2">
            Acciones rápidas
          </div>

          <div className="bg-white rounded-[14px] px-3.5 py-3 flex items-center gap-2.5 mb-2.5 border-[0.5px] border-[#0000000f]">
            <div className="w-9 h-9 bg-[#0D0D14] rounded-[10px] flex items-center justify-center flex-shrink-0">
              <svg viewBox="0 0 24 24" fill="none" className="w-[18px] h-[18px]">
                <rect x="3" y="3" width="7" height="7" rx="1" stroke="#FF4D2E" strokeWidth="1.5"/>
                <rect x="14" y="3" width="7" height="7" rx="1" stroke="#FF4D2E" strokeWidth="1.5"/>
                <rect x="3" y="14" width="7" height="7" rx="1" stroke="#FF4D2E" strokeWidth="1.5"/>
                <rect x="5" y="5" width="3" height="3" fill="#FF4D2E"/>
                <rect x="16" y="5" width="3" height="3" fill="#FF4D2E"/>
                <rect x="5" y="16" width="3" height="3" fill="#FF4D2E"/>
                <path d="M14 14h2v2h-2zM16 16h2v2h-2zM18 14h2v2h-2zM14 18h2v2h-2zM18 18h2v2h-2z" fill="#FF4D2E"/>
              </svg>
            </div>
            <div>
              <div className="text-xs font-medium text-[#0D0D14]">Escanear entrada</div>
              <div className="text-[10px] text-[#00000055]">Registra tu visita de hoy</div>
            </div>
          </div>

          <div className="bg-white rounded-[14px] px-3.5 py-3 flex items-center gap-2.5 border-[0.5px] border-[#0000000f]">
            <div className="w-9 h-9 bg-[#F5F4F0] rounded-[10px] flex items-center justify-center flex-shrink-0">
              <svg viewBox="0 0 24 24" fill="none" className="w-[18px] h-[18px]">
                <path d="M6 4h12M6 20h12M8 4v16M16 4v16M8 10h8M8 14h8" stroke="#0D0D14" strokeWidth="1.5" strokeLinecap="round"/>
              </svg>
            </div>
            <div>
              <div className="text-xs font-medium text-[#0D0D14]">Mis rutinas</div>
              <div className="text-[10px] text-[#00000055]">Ver entrenamiento asignado</div>
            </div>
          </div>
        </div>

        <div className="flex justify-around py-2.5 pb-3 bg-white border-t-[0.5px] border-[#0000000f] mt-3">
          <div className="flex flex-col items-center gap-1">
            <svg viewBox="0 0 24 24" fill="none" className="w-[18px] h-[18px]">
              <path d="M3 12L12 4l9 8" stroke="#FF4D2E" strokeWidth="1.5"/>
              <path d="M5 10v9a1 1 0 001 1h4v-5h4v5h4a1 1 0 001-1v-9" stroke="#FF4D2E" strokeWidth="1.5"/>
            </svg>
            <div className="text-[8px] text-[#FF4D2E]">Inicio</div>
            <div className="w-1 h-1 bg-[#FF4D2E] rounded-full"></div>
          </div>
          <div className="flex flex-col items-center gap-1">
            <svg viewBox="0 0 24 24" fill="none" className="w-[18px] h-[18px]">
              <rect x="3" y="3" width="7" height="7" rx="1" stroke="#ccc" strokeWidth="1.5"/>
              <rect x="14" y="3" width="7" height="7" rx="1" stroke="#ccc" strokeWidth="1.5"/>
              <rect x="3" y="14" width="7" height="7" rx="1" stroke="#ccc" strokeWidth="1.5"/>
              <rect x="5" y="5" width="3" height="3" fill="#ccc"/>
              <rect x="16" y="5" width="3" height="3" fill="#ccc"/>
              <rect x="5" y="16" width="3" height="3" fill="#ccc"/>
            </svg>
            <div className="text-[8px] text-[#00000044]">QR</div>
          </div>
          <div className="flex flex-col items-center gap-1">
            <svg viewBox="0 0 24 24" fill="none" className="w-[18px] h-[18px]">
              <path d="M3 17l4-8 4 5 3-3 4 6H3z" stroke="#ccc" strokeWidth="1.5" strokeLinejoin="round"/>
            </svg>
            <div className="text-[8px] text-[#00000044]">Máquinas</div>
          </div>
          <div className="flex flex-col items-center gap-1">
            <svg viewBox="0 0 24 24" fill="none" className="w-[18px] h-[18px]">
              <circle cx="12" cy="8" r="4" stroke="#ccc" strokeWidth="1.5"/>
              <path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" stroke="#ccc" strokeWidth="1.5" strokeLinecap="round"/>
            </svg>
            <div className="text-[8px] text-[#00000044]">Perfil</div>
          </div>
        </div>
      </div>
    </PhoneFrame>
  );
}
