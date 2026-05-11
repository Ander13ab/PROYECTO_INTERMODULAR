import { PhoneFrame } from './PhoneFrame';

export function QRSuccessMaquina() {
  return (
    <PhoneFrame statusBarColor="#ffffff55" size="flow">
      <div className="bg-[#0D0D14] min-h-[360px] px-3.5 py-3">
        <div className="flex items-center gap-2 mb-2.5">
          <div className="w-9 h-9 bg-[#2266FF22] rounded-[10px] flex items-center justify-center flex-shrink-0">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
              <path d="M6 4v16M18 4v16M6 8h12M6 16h12" stroke="#2266FF" strokeWidth="1.5" strokeLinecap="round"/>
            </svg>
          </div>
          <div>
            <div className="font-['Syne'] text-[13px] font-bold text-white">Press banca</div>
            <div className="text-[9px] text-[#2266FF]">Pectoral · Tríceps · Hombro</div>
          </div>
        </div>

        <div className="bg-[#1C1C28] rounded-xl h-[85px] mb-2.5 flex items-center justify-center overflow-hidden relative">
          <span className="text-[9px] text-[#ffffff22]">Imagen/vídeo de la máquina</span>
          <div className="absolute top-2 right-2 bg-[#2266FF] rounded-md px-[7px] py-0.5 text-[8px] text-white font-medium">
            Nivel medio
          </div>
        </div>

        <div className="mb-1.5">
          <span className="inline-block bg-[#2266FF22] text-[#5588FF] border-[0.5px] border-[#2266FF33] text-[8px] px-[7px] py-0.5 rounded-full mr-1 mb-1">
            Fuerza
          </span>
          <span className="inline-block bg-[#2266FF22] text-[#5588FF] border-[0.5px] border-[#2266FF33] text-[8px] px-[7px] py-0.5 rounded-full mr-1 mb-1">
            Compuesto
          </span>
          <span className="inline-block bg-[#2266FF22] text-[#5588FF] border-[0.5px] border-[#2266FF33] text-[8px] px-[7px] py-0.5 rounded-full mb-1">
            Pectoral
          </span>
        </div>

        <div className="space-y-1.5">
          <div className="flex gap-1.5 items-start">
            <div className="w-4 h-4 bg-[#2266FF] rounded-[5px] flex items-center justify-center text-[8px] font-bold text-white flex-shrink-0 mt-0.5">
              1
            </div>
            <p className="text-[9px] text-[#ffffff66] leading-tight">
              Ajusta el banco a posición horizontal y selecciona el peso.
            </p>
          </div>
          <div className="flex gap-1.5 items-start">
            <div className="w-4 h-4 bg-[#2266FF] rounded-[5px] flex items-center justify-center text-[8px] font-bold text-white flex-shrink-0 mt-0.5">
              2
            </div>
            <p className="text-[9px] text-[#ffffff66] leading-tight">
              Agarre a la anchura de hombros, codos a 45°.
            </p>
          </div>
          <div className="flex gap-1.5 items-start">
            <div className="w-4 h-4 bg-[#2266FF] rounded-[5px] flex items-center justify-center text-[8px] font-bold text-white flex-shrink-0 mt-0.5">
              3
            </div>
            <p className="text-[9px] text-[#ffffff66] leading-tight">
              Baja controlado hasta el pecho, empuja sin bloquear codos.
            </p>
          </div>
        </div>

        <div className="bg-[#E24B4A11] border-[0.5px] border-[#E24B4A22] rounded-lg px-2 py-1.5 mt-1.5">
          <div className="text-[8px] text-[#E24B4A88] font-medium mb-0.5">Aviso de seguridad</div>
          <p className="text-[9px] text-[#ffffff44] leading-tight">
            No realices este ejercicio sin un compañero si usas cargas elevadas.
          </p>
        </div>
      </div>
    </PhoneFrame>
  );
}
