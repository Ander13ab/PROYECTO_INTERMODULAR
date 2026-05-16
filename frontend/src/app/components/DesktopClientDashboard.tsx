function SidebarItem({
  label,
  active = false,
}: {
  label: string;
  active?: boolean;
}) {
  return (
    <div
      className={`rounded-2xl px-4 py-3 text-sm transition ${
        active
          ? 'bg-[#FF4D2E] font-semibold text-white'
          : 'bg-[#181B24] text-[#C9D0DB]'
      }`}
    >
      {label}
    </div>
  );
}

function StatCard({
  label,
  value,
  helper,
  accent,
}: {
  label: string;
  value: string;
  helper: string;
  accent: string;
}) {
  return (
    <div className="rounded-[24px] border border-[#E7EAEE] bg-white p-6">
      <div className="mb-5 flex items-center gap-3">
        <div className="h-8 w-2 rounded-full" style={{ backgroundColor: accent }} />
        <p className="text-sm font-semibold text-[#667085]">{label}</p>
      </div>
      <p className="font-['Syne'] text-4xl font-extrabold text-[#101828]">{value}</p>
      <p className="mt-3 text-sm text-[#98A2B3]">{helper}</p>
    </div>
  );
}

interface DesktopClientDashboardProps {
  userName: string;
  onLogout: () => void;
}

export function DesktopClientDashboard({
  userName,
  onLogout,
}: DesktopClientDashboardProps) {
  return (
    <div className="overflow-hidden rounded-[32px] border border-[#E7EAEE] bg-[#F2F5F2] shadow-[0_24px_60px_rgba(15,23,42,0.10)]">
      <div className="grid min-h-[760px] grid-cols-[240px_1fr]">
        <aside className="bg-[#0D0D14] px-5 py-7">
          <h2 className="font-['Syne'] text-3xl font-extrabold text-white">
            Hazel Gym
          </h2>
          <p className="mt-2 text-sm text-[#98A2B3]">Panel cliente</p>
          <div className="mt-10 space-y-3">
            <SidebarItem label="Inicio" active />
            <SidebarItem label="QR y asistencia" />
            <SidebarItem label="Maquinas" />
            <SidebarItem label="Rutinas" />
            <SidebarItem label="Perfil" />
          </div>
        </aside>

        <main className="p-10">
          <div className="mb-9 flex items-center justify-between gap-6">
            <div>
              <p className="text-sm text-[#667085]">Hola de nuevo</p>
              <h3 className="font-['Syne'] text-5xl font-extrabold text-[#101828]">
                {userName}
              </h3>
            </div>
            <div className="flex items-center gap-3">
              <div className="rounded-full bg-[#FFE1D8] px-5 py-3 text-sm font-semibold text-[#C2410C]">
                Cliente
              </div>
              <button
                className="rounded-full bg-[#0D0D14] px-5 py-3 text-sm font-semibold text-white"
                onClick={onLogout}
              >
                Cerrar sesion
              </button>
            </div>
          </div>

          <div className="grid grid-cols-3 gap-6">
            <StatCard
              accent="#FF4D2E"
              helper="Entradas registradas"
              label="Visitas totales"
              value="47"
            />
            <StatCard
              accent="#2266FF"
              helper="Dias consecutivos"
              label="Racha actual"
              value="14"
            />
            <StatCard
              accent="#22CC66"
              helper="Asignadas por tu entrenador"
              label="Rutinas activas"
              value="3"
            />
          </div>

          <div className="mt-8 grid grid-cols-[1fr_320px] gap-6">
            <section className="rounded-[28px] border border-[#E7EAEE] bg-white p-7">
              <div className="mb-6 flex items-center justify-between">
                <h4 className="font-['Syne'] text-2xl font-bold text-[#101828]">
                  Acciones rapidas
                </h4>
                <p className="text-sm text-[#98A2B3]">
                  Proxima conexion con asistencias, maquinas y rutinas
                </p>
              </div>
              <div className="grid grid-cols-3 gap-5">
                <div className="rounded-[22px] bg-[#0D0D14] p-6 text-white">
                  <p className="text-lg font-semibold">Escanear entrada</p>
                  <p className="mt-3 text-sm text-white/55">
                    Registro de asistencia con QR
                  </p>
                </div>
                <div className="rounded-[22px] bg-[#F8FAFC] p-6">
                  <p className="text-lg font-semibold text-[#101828]">Ver maquinas</p>
                  <p className="mt-3 text-sm text-[#98A2B3]">
                    Catalogo y estado actual
                  </p>
                </div>
                <div className="rounded-[22px] bg-[#F8FAFC] p-6">
                  <p className="text-lg font-semibold text-[#101828]">Mis rutinas</p>
                  <p className="mt-3 text-sm text-[#98A2B3]">
                    Entrenamiento asignado
                  </p>
                </div>
              </div>
            </section>

            <aside className="rounded-[28px] bg-[#0D0D14] p-7 text-white">
              <h4 className="font-['Syne'] text-2xl font-bold">Proximas clases</h4>
              <div className="mt-6 space-y-4">
                <div className="rounded-[20px] bg-[#181B24] p-5">
                  <p className="text-lg font-semibold">Yoga</p>
                  <p className="mt-2 text-sm text-white/55">Hoy · 18:00 · Sala 2</p>
                </div>
                <div className="rounded-[20px] bg-[#181B24] p-5">
                  <p className="text-lg font-semibold">Spinning</p>
                  <p className="mt-2 text-sm text-white/55">
                    Manana · 08:30 · Sala 1
                  </p>
                </div>
              </div>
            </aside>
          </div>
        </main>
      </div>
    </div>
  );
}
