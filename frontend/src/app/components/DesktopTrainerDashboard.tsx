function TrainerSidebarItem({
  label,
  active = false,
}: {
  label: string;
  active?: boolean;
}) {
  return (
    <div
      className={`rounded-2xl px-4 py-3 text-sm ${
        active
          ? 'bg-[#2266FF] font-semibold text-white'
          : 'bg-[#10245E] text-[#D8E4FF]'
      }`}
    >
      {label}
    </div>
  );
}

interface DesktopTrainerDashboardProps {
  userName: string;
  onLogout: () => void;
}

export function DesktopTrainerDashboard({
  userName,
  onLogout,
}: DesktopTrainerDashboardProps) {
  return (
    <div className="overflow-hidden rounded-[32px] border border-[#DCE3F6] bg-[#EEF2FF] shadow-[0_24px_60px_rgba(15,23,42,0.10)]">
      <div className="grid min-h-[760px] grid-cols-[240px_1fr]">
        <aside className="bg-[#0A1A4A] px-5 py-7">
          <h2 className="font-['Syne'] text-3xl font-extrabold text-white">
            Hazel Gym
          </h2>
          <p className="mt-2 text-sm text-[#C7D7FE]">Panel entrenador</p>
          <div className="mt-10 space-y-3">
            <TrainerSidebarItem label="Resumen" active />
            <TrainerSidebarItem label="Sesiones" />
            <TrainerSidebarItem label="Clientes" />
            <TrainerSidebarItem label="Rutinas" />
            <TrainerSidebarItem label="Perfil" />
          </div>
        </aside>

        <main className="p-10">
          <div className="mb-9 flex items-center justify-between gap-6">
            <div>
              <p className="text-sm text-[#667085]">Panel de entrenador</p>
              <h3 className="font-['Syne'] text-5xl font-extrabold text-[#0A1A4A]">
                {userName}
              </h3>
            </div>
            <div className="flex items-center gap-3">
              <div className="rounded-full bg-[#DBEAFE] px-5 py-3 text-sm font-semibold text-[#1D4ED8]">
                Entrenador
              </div>
              <button
                className="rounded-full bg-[#0A1A4A] px-5 py-3 text-sm font-semibold text-white"
                onClick={onLogout}
              >
                Cerrar sesion
              </button>
            </div>
          </div>

          <div className="grid grid-cols-[1.1fr_0.9fr] gap-6">
            <section className="rounded-[28px] border border-[#DCE3F6] bg-white p-7">
              <div className="mb-6 flex items-center justify-between">
                <h4 className="font-['Syne'] text-2xl font-bold text-[#0A1A4A]">
                  Clases de hoy
                </h4>
                <div className="rounded-full bg-[#EEF2FF] px-4 py-2 text-sm font-semibold text-[#2266FF]">
                  3 sesiones
                </div>
              </div>

              <div className="space-y-4">
                {[
                  ['08:30', 'Spinning', '#2266FF'],
                  ['10:00', 'HIIT', '#FF4D2E'],
                  ['18:00', 'Yoga', '#BFC7D4'],
                ].map(([hour, name, color]) => (
                  <div
                    key={`${hour}-${name}`}
                    className="flex items-center gap-4 rounded-2xl border border-[#EEF2F7] bg-[#F8FAFC] px-5 py-4"
                  >
                    <span className="w-16 text-sm text-[#667085]">{hour}</span>
                    <div
                      className="h-3 w-3 rounded-full"
                      style={{ backgroundColor: color }}
                    />
                    <span className="text-base font-semibold text-[#0A1A4A]">
                      {name}
                    </span>
                  </div>
                ))}
              </div>

              <button className="mt-6 w-full rounded-[20px] bg-[#2266FF] px-5 py-4 font-['Syne'] text-lg font-bold text-white">
                Escanear QR de sesion
              </button>
            </section>

            <div className="space-y-6">
              <section className="rounded-[28px] border border-[#DCE3F6] bg-white p-7">
                <div className="mb-6 flex items-center justify-between">
                  <h4 className="font-['Syne'] text-2xl font-bold text-[#0A1A4A]">
                    Mis clientes
                  </h4>
                  <div className="rounded-full bg-[#FFF0EC] px-4 py-2 text-sm font-semibold text-[#FF4D2E]">
                    12 activos
                  </div>
                </div>
                <div className="flex flex-wrap gap-3">
                  {['Ana G.', 'Marcos T.', 'Elena S.', '+9 mas'].map((name, index) => (
                    <div
                      key={name}
                      className={`rounded-full px-4 py-2 text-sm ${
                        index === 3
                          ? 'bg-[#F5F5F5] text-[#667085]'
                          : 'bg-[#EEF2FF] text-[#2266FF]'
                      }`}
                    >
                      {name}
                    </div>
                  ))}
                </div>
              </section>

              <section className="rounded-[28px] border border-[#DCE3F6] bg-white p-7">
                <h4 className="font-['Syne'] text-2xl font-bold text-[#0A1A4A]">
                  Clases este mes
                </h4>
                <div className="mt-6 flex items-end gap-3">
                  <span className="font-['Syne'] text-6xl font-extrabold text-[#0A1A4A]">
                    22
                  </span>
                  <span className="pb-2 text-lg text-[#98A2B3]">/ 30 objetivo</span>
                </div>
              </section>
            </div>
          </div>
        </main>
      </div>
    </div>
  );
}
