import { PhoneFrame } from './PhoneFrame';

export function AdminHomeScreen() {
  return (
    <PhoneFrame statusBarColor="#00000066">
      <div className="bg-[#F2F5F2] min-h-[360px]">
        <div className="bg-[#0D2010] px-4 pt-3.5 pb-[18px] rounded-b-[20px]">
          <div className="text-[10px] text-[#ffffff44] mb-1">Panel de administración</div>
          <div className="font-['Syne'] text-base font-extrabold text-white">GymApp</div>
          <div className="inline-block bg-[#22CC66] rounded-full px-2 py-1 mt-1.5 text-[9px] text-[#0D2010] font-bold">
            Administrador
          </div>
        </div>

        <div className="px-3.5 pt-3">
          <div className="grid grid-cols-2 gap-2 mb-2.5">
            <div className="bg-white rounded-xl px-2.5 py-2.5 border-[0.5px] border-[#0000000f]">
              <div className="font-['Syne'] text-[18px] font-extrabold text-[#0D2010]">148</div>
              <div className="text-[9px] text-[#00000055]">Socios activos</div>
            </div>
            <div className="bg-white rounded-xl px-2.5 py-2.5 border-[0.5px] border-[#0000000f]">
              <div className="font-['Syne'] text-[18px] font-extrabold text-[#0D2010]">12</div>
              <div className="text-[9px] text-[#00000055]">Entrenadores</div>
            </div>
            <div className="bg-white rounded-xl px-2.5 py-2.5 border-[0.5px] border-[#0000000f]">
              <div className="font-['Syne'] text-[18px] font-extrabold text-[#0D2010]">31</div>
              <div className="text-[9px] text-[#00000055]">Máquinas</div>
            </div>
            <div className="bg-white rounded-xl px-2.5 py-2.5 border-[0.5px] border-[#0000000f]">
              <div className="font-['Syne'] text-[18px] font-extrabold text-[#0D2010]">7</div>
              <div className="text-[9px] text-[#00000055]">Clases hoy</div>
            </div>
          </div>

          <div className="flex flex-col gap-1.5">
            <div className="bg-white rounded-xl px-3 py-2.5 flex items-center gap-2.5 border-[0.5px] border-[#0000000f]">
              <div className="w-7 h-7 rounded-lg bg-[#2266FF22] flex items-center justify-center flex-shrink-0">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
                  <circle cx="9" cy="8" r="3.5" stroke="#2266FF" strokeWidth="1.5"/>
                  <path d="M3 19c0-3.3 2.7-6 6-6h.5" stroke="#2266FF" strokeWidth="1.5" strokeLinecap="round"/>
                  <path d="M15 13h6M18 10v6" stroke="#2266FF" strokeWidth="1.5" strokeLinecap="round"/>
                </svg>
              </div>
              <div className="flex-1">
                <div className="text-xs font-medium text-[#0D2010]">Gestión de usuarios</div>
                <div className="text-[9px] text-[#00000055]">Roles, altas y bajas</div>
              </div>
              <div className="text-xs text-[#00000033]">›</div>
            </div>

            <div className="bg-white rounded-xl px-3 py-2.5 flex items-center gap-2.5 border-[0.5px] border-[#0000000f]">
              <div className="w-7 h-7 rounded-lg bg-[#22CC6622] flex items-center justify-center flex-shrink-0">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
                  <rect x="3" y="3" width="7" height="7" rx="1" stroke="#22CC66" strokeWidth="1.5"/>
                  <rect x="14" y="3" width="7" height="7" rx="1" stroke="#22CC66" strokeWidth="1.5"/>
                  <rect x="3" y="14" width="7" height="7" rx="1" stroke="#22CC66" strokeWidth="1.5"/>
                  <rect x="5" y="5" width="3" height="3" fill="#22CC66"/>
                  <rect x="16" y="5" width="3" height="3" fill="#22CC66"/>
                  <rect x="5" y="16" width="3" height="3" fill="#22CC66"/>
                </svg>
              </div>
              <div className="flex-1">
                <div className="text-xs font-medium text-[#0D2010]">Generar QR</div>
                <div className="text-[9px] text-[#00000055]">Entrada, máquinas y clases</div>
              </div>
              <div className="text-xs text-[#00000033]">›</div>
            </div>

            <div className="bg-white rounded-xl px-3 py-2.5 flex items-center gap-2.5 border-[0.5px] border-[#0000000f]">
              <div className="w-7 h-7 rounded-lg bg-[#FF4D2E22] flex items-center justify-center flex-shrink-0">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
                  <path d="M6 4h12M6 20h12M8 4v16M16 4v16M8 10h8M8 14h8" stroke="#FF4D2E" strokeWidth="1.5" strokeLinecap="round"/>
                </svg>
              </div>
              <div className="flex-1">
                <div className="text-xs font-medium text-[#0D2010]">Máquinas</div>
                <div className="text-[9px] text-[#00000055]">Catálogo y estado</div>
              </div>
              <div className="text-xs text-[#00000033]">›</div>
            </div>
          </div>
        </div>
      </div>
    </PhoneFrame>
  );
}
