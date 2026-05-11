import { PhoneFrame } from './PhoneFrame';

export function QRErrorScreen() {
  return (
    <PhoneFrame statusBarColor="#ffffff44" size="flow">
      <div className="bg-[#160A0A] min-h-[360px] px-4 py-[22px] text-center">
        <div className="h-2.5"></div>
        <div className="w-[52px] h-[52px] bg-[#E24B4A22] border-[1.5px] border-[#E24B4A44] rounded-full flex items-center justify-center mx-auto mb-3">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none">
            <path d="M18 6L6 18M6 6l12 12" stroke="#E24B4A" strokeWidth="2.5" strokeLinecap="round"/>
          </svg>
        </div>
        <div className="font-['Syne'] text-[14px] font-extrabold text-white mb-1">QR no válido</div>
        <p className="text-[10px] text-[#ffffff33] mb-3.5 leading-tight">
          No se pudo registrar<br />la entrada en este momento
        </p>

        <div className="bg-[#1C1010] border-[0.5px] border-[#E24B4A22] rounded-xl px-3 py-2.5 mb-3 text-left">
          <div className="text-[9px] text-[#E24B4A88] font-medium mb-1.5">Posibles causas</div>
          <div className="space-y-0.5">
            <div className="flex items-center gap-1.5 py-0.5">
              <div className="w-1 h-1 bg-[#E24B4A55] rounded-full flex-shrink-0"></div>
              <p className="text-[9px] text-[#ffffff44]">QR caducado o ya usado hoy</p>
            </div>
            <div className="flex items-center gap-1.5 py-0.5">
              <div className="w-1 h-1 bg-[#E24B4A55] rounded-full flex-shrink-0"></div>
              <p className="text-[9px] text-[#ffffff44]">El QR no pertenece a esta sede</p>
            </div>
            <div className="flex items-center gap-1.5 py-0.5">
              <div className="w-1 h-1 bg-[#E24B4A55] rounded-full flex-shrink-0"></div>
              <p className="text-[9px] text-[#ffffff44]">Sin conexión al servidor</p>
            </div>
            <div className="flex items-center gap-1.5 py-0.5">
              <div className="w-1 h-1 bg-[#E24B4A55] rounded-full flex-shrink-0"></div>
              <p className="text-[9px] text-[#ffffff44]">Cuenta inactiva o suspendida</p>
            </div>
          </div>
        </div>

        <button className="w-full bg-[#E24B4A] rounded-[10px] py-2.5 text-center font-['Syne'] text-[11px] font-bold text-white mb-1.5">
          Intentar de nuevo
        </button>
        <button className="w-full border-[0.5px] border-[#ffffff22] rounded-[10px] py-2 text-center text-[10px] text-[#ffffff44]">
          Volver al inicio
        </button>
      </div>
    </PhoneFrame>
  );
}
