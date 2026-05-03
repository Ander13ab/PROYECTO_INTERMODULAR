import { PhoneFrame } from './PhoneFrame';

export function TrainerHomeScreen() {
  return (
    <PhoneFrame statusBarColor="#00000066">
      <div className="bg-[#F0F4FF] min-h-[360px]">
        <div className="bg-[#0A1A4A] px-4 pt-3.5 pb-[18px] rounded-b-[20px]">
          <div className="text-[10px] text-[#ffffff44] mb-1">Panel de entrenador</div>
          <div className="font-['Syne'] text-base font-extrabold text-white">Laura R.</div>
          <div className="inline-block bg-[#2266FF] rounded-full px-2 py-1 mt-1.5 text-[9px] text-[#C5D5FF] font-medium">
            Entrenadora certificada
          </div>
        </div>

        <div className="px-3.5 pt-3">
          <div className="bg-white rounded-[14px] px-3.5 py-3 mb-2 border-[0.5px] border-[#0000000f]">
            <div className="flex justify-between items-center mb-1.5">
              <span className="text-xs font-medium text-[#0A1A4A]">Clases de hoy</span>
              <span className="bg-[#EEF2FF] text-[#2266FF] text-[9px] px-[7px] py-0.5 rounded-full font-medium">
                3 sesiones
              </span>
            </div>
            <div className="space-y-1">
              <div className="flex items-center gap-2 py-1.5 border-b-[0.5px] border-[#0000000a]">
                <span className="text-[9px] text-[#00000044] w-[30px] flex-shrink-0">08:30</span>
                <div className="w-1.5 h-1.5 rounded-full bg-[#2266FF] flex-shrink-0"></div>
                <span className="text-[11px] text-[#0A1A4A]">Spinning</span>
              </div>
              <div className="flex items-center gap-2 py-1.5 border-b-[0.5px] border-[#0000000a]">
                <span className="text-[9px] text-[#00000044] w-[30px] flex-shrink-0">10:00</span>
                <div className="w-1.5 h-1.5 rounded-full bg-[#FF4D2E] flex-shrink-0"></div>
                <span className="text-[11px] text-[#0A1A4A]">HIIT</span>
              </div>
              <div className="flex items-center gap-2 py-1.5">
                <span className="text-[9px] text-[#00000044] w-[30px] flex-shrink-0">18:00</span>
                <div className="w-1.5 h-1.5 rounded-full bg-[#cccccc] flex-shrink-0"></div>
                <span className="text-[11px] text-[#0A1A4A]">Yoga</span>
              </div>
            </div>
            <button className="w-full bg-[#2266FF] rounded-[10px] py-2.5 text-center font-['Syne'] text-[11px] font-bold text-white mt-1 tracking-wide">
              Escanear QR de sesión
            </button>
          </div>

          <div className="bg-white rounded-[14px] px-3.5 py-3 mb-2 border-[0.5px] border-[#0000000f]">
            <div className="flex justify-between items-center mb-1.5">
              <span className="text-xs font-medium text-[#0A1A4A]">Mis clientes</span>
              <span className="bg-[#FFF0EC] text-[#FF4D2E] text-[9px] px-[7px] py-0.5 rounded-full font-medium">
                12 activos
              </span>
            </div>
            <div className="flex gap-1.5 flex-wrap">
              <div className="text-[10px] bg-[#EEF2FF] text-[#2266FF] px-2 py-1 rounded-full">Ana G.</div>
              <div className="text-[10px] bg-[#EEF2FF] text-[#2266FF] px-2 py-1 rounded-full">Marcos T.</div>
              <div className="text-[10px] bg-[#EEF2FF] text-[#2266FF] px-2 py-1 rounded-full">Elena S.</div>
              <div className="text-[10px] bg-[#F5F5F5] text-[#888] px-2 py-1 rounded-full">+9 más</div>
            </div>
          </div>

          <div className="bg-white rounded-[14px] px-3.5 py-3 border-[0.5px] border-[#0000000f]">
            <div className="text-xs font-medium text-[#0A1A4A] mb-1">Clases este mes</div>
            <div className="flex items-baseline gap-1.5">
              <span className="font-['Syne'] text-[28px] font-extrabold text-[#0A1A4A]">22</span>
              <span className="text-[11px] text-[#00000055]">/ 30 objetivo</span>
            </div>
          </div>
        </div>
      </div>
    </PhoneFrame>
  );
}
