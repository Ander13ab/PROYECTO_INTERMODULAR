import { useEffect, useMemo, useState } from 'react';
import type { ReactNode } from 'react';
import {
  createGymClass,
  createMembershipFee,
  createQrCode,
  createUser,
  createMachine,
  deleteGymClass,
  deleteMembershipFee,
  deleteQrCode,
  deleteUser,
  deleteMachine,
  getAdminDashboardData,
  updateGymClass,
  updateMembershipFee,
  updateQrCode,
  updateUser,
  updateMachine,
} from '../services/adminDashboardService';
import type {
  AdminDashboardData,
  AttendanceSummary,
  ClassSessionSummary,
  GymClassDraft,
  GymClassSummary,
  MachineDraft,
  MachineSummary,
  MembershipFeeDraft,
  MembershipFeeSummary,
  QrCodeDraft,
  QrCodeSummary,
  UserDraft,
  UserSummary,
} from '../types/admin';

type AdminSection = 'dashboard' | 'usuarios' | 'qr' | 'maquinas' | 'clases' | 'cuotas';

const EMPTY_MACHINE_DRAFT: MachineDraft = {
  advertenciaSeguridad: '',
  descripcion: '',
  estado: 'ACTIVA',
  grupoMuscular: '',
  imagenUrl: '',
  instrucciones: '',
  nivel: '',
  nombre: '',
};

const EMPTY_USER_DRAFT: UserDraft = {
  activo: true,
  email: '',
  nombre: '',
  password: '',
  roleName: 'CLIENT',
};

const EMPTY_GYM_CLASS_DRAFT: GymClassDraft = {
  activa: true,
  descripcion: '',
  duracion: '60',
  entrenadorId: '',
  nombre: '',
};

const EMPTY_MEMBERSHIP_FEE_DRAFT: MembershipFeeDraft = {
  descripcion: '',
  nombre: '',
  precio: '',
};

const EMPTY_QR_CODE_DRAFT: QrCodeDraft = {
  esEntradaGimnasio: true,
  maquinaId: '',
  sesionClaseId: '',
  tipo: 'ENTRY',
};

function AdminSidebarItem({
  label,
  active = false,
  onClick,
}: {
  label: string;
  active?: boolean;
  onClick: () => void;
}) {
  return (
    <button
      className={`w-full rounded-2xl border px-4 py-3 text-left text-sm transition duration-200 ${
        active
          ? 'border-[#7BE7A8] bg-[#22CC66] font-semibold text-[#0D2010] shadow-[0_12px_24px_rgba(34,204,102,0.28)]'
          : 'border-[#1F4D2F] bg-[#133B21] text-[#D1FAE5] hover:bg-[#184827]'
      }`}
      onClick={onClick}
      type="button"
    >
      {label}
    </button>
  );
}

function AdminMetric({
  label,
  value,
}: {
  label: string;
  value: string;
}) {
  return (
    <div className="rounded-[22px] border border-white/70 bg-white/88 p-6 shadow-[0_22px_50px_rgba(16,24,40,0.08)] backdrop-blur-sm">
      <p className="text-sm text-[#667085]">{label}</p>
      <p className="mt-4 font-['Syne'] text-4xl font-extrabold text-[#0D2010]">
        {value}
      </p>
    </div>
  );
}

function AdminInfoCard({
  title,
  subtitle,
}: {
  title: string;
  subtitle: string;
}) {
  return (
    <div className="rounded-[20px] border border-white/70 bg-white/88 px-5 py-4 shadow-[0_14px_28px_rgba(16,24,40,0.06)] backdrop-blur-sm">
      <p className="text-lg font-semibold text-[#0D2010]">{title}</p>
      <p className="mt-2 text-sm text-[#98A2B3]">{subtitle}</p>
    </div>
  );
}

function AdminListCard({
  title,
  description,
  children,
}: {
  title: string;
  description: string;
  children: ReactNode;
}) {
  return (
    <section className="rounded-[28px] border border-white/70 bg-white/88 p-5 shadow-[0_24px_50px_rgba(16,24,40,0.08)] backdrop-blur-sm sm:p-6 xl:p-7">
      <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
        <div>
          <h4 className="font-['Syne'] text-2xl font-bold text-[#0D2010]">
            {title}
          </h4>
          <p className="mt-2 text-sm text-[#98A2B3]">{description}</p>
        </div>
      </div>
      <div className="space-y-4">{children}</div>
    </section>
  );
}

function AdminTextField({
  label,
  value,
  onChange,
  placeholder,
  type = 'text',
}: {
  label: string;
  value: string;
  onChange: (value: string) => void;
  placeholder: string;
  type?: 'text' | 'email' | 'password';
}) {
  return (
    <label className="block">
      <span className="mb-2 block text-sm font-semibold text-[#667085]">{label}</span>
      <input
        className="w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66] focus:bg-white focus:shadow-[0_0_0_4px_rgba(34,204,102,0.10)]"
        placeholder={placeholder}
        type={type}
        value={value}
        onChange={(event) => onChange(event.target.value)}
      />
    </label>
  );
}

function countActiveClients(users: UserSummary[]) {
  return users.filter((user) => user.role === 'CLIENT' && user.activo).length;
}

function countTrainers(users: UserSummary[]) {
  return users.filter((user) => user.role === 'TRAINER').length;
}

function countActiveClasses(data: AdminDashboardData) {
  return data.classes.filter((gymClass) => gymClass.activa).length;
}

function formatActivityTime(dateTime: string) {
  const date = new Date(dateTime);

  return new Intl.DateTimeFormat('es-ES', {
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    month: '2-digit',
  }).format(date);
}

function buildRecentActivity(attendances: AttendanceSummary[]) {
  return [...attendances]
    .sort(
      (first, second) =>
        new Date(second.fechaHora).getTime() - new Date(first.fechaHora).getTime(),
    )
    .slice(0, 6)
    .map((attendance) => ({
      subtitle: `${attendance.usuarioNombre} - ${formatActivityTime(attendance.fechaHora)}`,
      title: `Asistencia QR ${attendance.qrType.toLowerCase()}`,
    }));
}

function buildQrStats(attendances: AttendanceSummary[]) {
  const byType = new Map<string, number>();

  attendances.forEach((attendance) => {
    byType.set(attendance.qrType, (byType.get(attendance.qrType) ?? 0) + 1);
  });

  return [...byType.entries()]
    .map(([type, count]) => ({ count, type }))
    .sort((first, second) => second.count - first.count);
}

function formatPrice(value: number) {
  return new Intl.NumberFormat('es-ES', {
    currency: 'EUR',
    style: 'currency',
  }).format(value);
}

function machineToDraft(machine: MachineSummary): MachineDraft {
  return {
    advertenciaSeguridad: machine.advertenciaSeguridad ?? '',
    descripcion: machine.descripcion ?? '',
    estado: machine.estado,
    grupoMuscular: machine.grupoMuscular ?? '',
    imagenUrl: machine.imagenUrl ?? '',
    instrucciones: machine.instrucciones ?? '',
    nivel: machine.nivel ?? '',
    nombre: machine.nombre,
  };
}

function gymClassToDraft(gymClass: GymClassSummary): GymClassDraft {
  return {
    activa: gymClass.activa,
    descripcion: gymClass.descripcion ?? '',
    duracion: gymClass.duracion ? String(gymClass.duracion) : '60',
    entrenadorId: gymClass.entrenadorId ? String(gymClass.entrenadorId) : '',
    nombre: gymClass.nombre,
  };
}

function membershipFeeToDraft(fee: MembershipFeeSummary): MembershipFeeDraft {
  return {
    descripcion: fee.descripcion ?? '',
    nombre: fee.nombre,
    precio: String(fee.precio),
  };
}

function qrCodeToDraft(qrCode: QrCodeSummary): QrCodeDraft {
  return {
    esEntradaGimnasio: qrCode.esEntradaGimnasio,
    maquinaId: qrCode.maquinaId ? String(qrCode.maquinaId) : '',
    sesionClaseId: qrCode.sesionClaseId ? String(qrCode.sesionClaseId) : '',
    tipo: qrCode.tipo,
  };
}

interface DesktopAdminDashboardProps {
  userName: string;
  onLogout: () => void;
}

export function DesktopAdminDashboard({
  userName,
  onLogout,
}: DesktopAdminDashboardProps) {
  const [activeSection, setActiveSection] = useState<AdminSection>('dashboard');
  const [data, setData] = useState<AdminDashboardData | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [machineDraft, setMachineDraft] = useState<MachineDraft>(EMPTY_MACHINE_DRAFT);
  const [selectedMachineId, setSelectedMachineId] = useState<number | null>(null);
  const [machineMessage, setMachineMessage] = useState<string | null>(null);
  const [isSavingMachine, setIsSavingMachine] = useState(false);
  const [userDraft, setUserDraft] = useState<UserDraft>(EMPTY_USER_DRAFT);
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
  const [userMessage, setUserMessage] = useState<string | null>(null);
  const [isSavingUser, setIsSavingUser] = useState(false);
  const [userSearch, setUserSearch] = useState('');
  const [classDraft, setClassDraft] = useState<GymClassDraft>(EMPTY_GYM_CLASS_DRAFT);
  const [selectedClassId, setSelectedClassId] = useState<number | null>(null);
  const [classMessage, setClassMessage] = useState<string | null>(null);
  const [isSavingClass, setIsSavingClass] = useState(false);
  const [classSearch, setClassSearch] = useState('');
  const [feeDraft, setFeeDraft] = useState<MembershipFeeDraft>(EMPTY_MEMBERSHIP_FEE_DRAFT);
  const [selectedFeeId, setSelectedFeeId] = useState<number | null>(null);
  const [feeMessage, setFeeMessage] = useState<string | null>(null);
  const [isSavingFee, setIsSavingFee] = useState(false);
  const [feeSearch, setFeeSearch] = useState('');
  const [qrDraft, setQrDraft] = useState<QrCodeDraft>(EMPTY_QR_CODE_DRAFT);
  const [selectedQrId, setSelectedQrId] = useState<number | null>(null);
  const [qrMessage, setQrMessage] = useState<string | null>(null);
  const [isSavingQr, setIsSavingQr] = useState(false);
  const [qrSearch, setQrSearch] = useState('');

  const loadDashboard = async () => {
    try {
      setIsLoading(true);
      setErrorMessage(null);
      const dashboardData = await getAdminDashboardData();
      setData(dashboardData);
    } catch (error) {
      setErrorMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido cargar el panel de administrador.',
      );
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    void loadDashboard();
  }, []);

  const activeClients = data ? countActiveClients(data.users) : '--';
  const trainers = data ? countTrainers(data.users) : '--';
  const machines = data ? data.machines.length : '--';
  const activeClasses = data ? countActiveClasses(data) : '--';
  const recentActivity = data ? buildRecentActivity(data.attendances) : [];
  const qrStats = useMemo(
    () => (data ? buildQrStats(data.attendances) : []),
    [data],
  );
  const filteredUsers = useMemo(() => {
    if (!data) {
      return [];
    }

    const search = userSearch.trim().toLowerCase();

    if (!search) {
      return data.users;
    }

    return data.users.filter((user) =>
      [user.nombre, user.email, user.role, String(user.id)]
        .join(' ')
        .toLowerCase()
        .includes(search),
    );
  }, [data, userSearch]);
  const trainersList = useMemo(
    () => data?.users.filter((user) => user.role === 'TRAINER') ?? [],
    [data],
  );
  const filteredClasses = useMemo(() => {
    if (!data) {
      return [];
    }

    const search = classSearch.trim().toLowerCase();

    if (!search) {
      return data.classes;
    }

    return data.classes.filter((gymClass) =>
      [
        gymClass.nombre,
        gymClass.descripcion ?? '',
        gymClass.entrenadorNombre ?? '',
        String(gymClass.id),
      ]
        .join(' ')
        .toLowerCase()
        .includes(search),
    );
  }, [classSearch, data]);
  const filteredFees = useMemo(() => {
    if (!data) {
      return [];
    }

    const search = feeSearch.trim().toLowerCase();

    if (!search) {
      return data.membershipFees;
    }

    return data.membershipFees.filter((fee) =>
      [fee.nombre, fee.descripcion ?? '', formatPrice(fee.precio), String(fee.id)]
        .join(' ')
        .toLowerCase()
        .includes(search),
    );
  }, [data, feeSearch]);
  const filteredQrCodes = useMemo(() => {
    if (!data) {
      return [];
    }

    const search = qrSearch.trim().toLowerCase();

    if (!search) {
      return data.qrCodes;
    }

    return data.qrCodes.filter((qrCode) =>
      [
        qrCode.tipo,
        qrCode.maquinaNombre ?? '',
        qrCode.sesionClaseResumen ?? '',
        qrCode.esEntradaGimnasio ? 'entrada' : '',
        String(qrCode.id),
      ]
        .join(' ')
        .toLowerCase()
        .includes(search),
    );
  }, [data, qrSearch]);

  const userRoleLabel: Record<UserDraft['roleName'], string> = {
    ADMIN: 'Administrador',
    CLIENT: 'Cliente',
    TRAINER: 'Entrenador',
  };
  const qrTypeLabel: Record<QrCodeDraft['tipo'], string> = {
    CLASS_SESSION: 'Sesion de clase',
    ENTRY: 'Entrada',
    MACHINE: 'Maquina',
  };

  const resetMachineEditor = () => {
    setSelectedMachineId(null);
    setMachineDraft(EMPTY_MACHINE_DRAFT);
    setMachineMessage(null);
  };

  const resetUserEditor = () => {
    setSelectedUserId(null);
    setUserDraft(EMPTY_USER_DRAFT);
    setUserMessage(null);
    setUserSearch('');
  };

  const resetClassEditor = () => {
    setSelectedClassId(null);
    setClassDraft(EMPTY_GYM_CLASS_DRAFT);
    setClassMessage(null);
    setClassSearch('');
  };

  const resetFeeEditor = () => {
    setSelectedFeeId(null);
    setFeeDraft(EMPTY_MEMBERSHIP_FEE_DRAFT);
    setFeeMessage(null);
    setFeeSearch('');
  };

  const resetQrEditor = () => {
    setSelectedQrId(null);
    setQrDraft(EMPTY_QR_CODE_DRAFT);
    setQrMessage(null);
    setQrSearch('');
  };

  const selectMachine = (machine: MachineSummary) => {
    setSelectedMachineId(machine.id);
    setMachineDraft(machineToDraft(machine));
    setMachineMessage(`Editando la maquina ${machine.nombre}.`);
  };

  const selectUser = (user: UserSummary) => {
    setSelectedUserId(user.id);
    setUserDraft({
      activo: user.activo,
      email: user.email,
      nombre: user.nombre,
      password: '',
      roleName: user.role as UserDraft['roleName'],
    });
    setUserMessage(`Editando el usuario ${user.nombre}.`);
  };

  const selectClass = (gymClass: GymClassSummary) => {
    setSelectedClassId(gymClass.id);
    setClassDraft(gymClassToDraft(gymClass));
    setClassMessage(`Editando la clase ${gymClass.nombre}.`);
  };

  const selectFee = (fee: MembershipFeeSummary) => {
    setSelectedFeeId(fee.id);
    setFeeDraft(membershipFeeToDraft(fee));
    setFeeMessage(`Editando la cuota ${fee.nombre}.`);
  };

  const selectQrCode = (qrCode: QrCodeSummary) => {
    setSelectedQrId(qrCode.id);
    setQrDraft(qrCodeToDraft(qrCode));
    setQrMessage(`Editando el QR #${qrCode.id}.`);
  };

  const handleMachineSave = async () => {
    if (!machineDraft.nombre.trim()) {
      setMachineMessage('El nombre de la maquina es obligatorio.');
      return;
    }

    try {
      setIsSavingMachine(true);
      setMachineMessage(null);

      if (selectedMachineId) {
        await updateMachine(selectedMachineId, machineDraft);
        setMachineMessage('Maquina actualizada correctamente.');
      } else {
        await createMachine(machineDraft);
        setMachineMessage('Maquina creada correctamente.');
      }

      await loadDashboard();
      setSelectedMachineId(null);
      setMachineDraft(EMPTY_MACHINE_DRAFT);
    } catch (error) {
      setMachineMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido guardar la maquina.',
      );
    } finally {
      setIsSavingMachine(false);
    }
  };

  const handleMachineDelete = async () => {
    if (!selectedMachineId) {
      setMachineMessage('Selecciona una maquina antes de intentar eliminarla.');
      return;
    }

    try {
      setIsSavingMachine(true);
      setMachineMessage(null);
      await deleteMachine(selectedMachineId);
      await loadDashboard();
      resetMachineEditor();
      setMachineMessage('Maquina eliminada correctamente.');
    } catch (error) {
      setMachineMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido eliminar la maquina.',
      );
    } finally {
      setIsSavingMachine(false);
    }
  };

  const handleUserSave = async () => {
    if (!userDraft.nombre.trim() || !userDraft.email.trim()) {
      setUserMessage('El nombre y el email del usuario son obligatorios.');
      return;
    }

    if (!selectedUserId && userDraft.password.trim().length < 6) {
      setUserMessage('La contrasena inicial debe tener al menos 6 caracteres.');
      return;
    }

    try {
      setIsSavingUser(true);
      setUserMessage(null);

      if (selectedUserId) {
        await updateUser(selectedUserId, userDraft);
        setUserMessage('Usuario actualizado correctamente.');
      } else {
        await createUser(userDraft);
        setUserMessage('Usuario creado correctamente.');
      }

      await loadDashboard();
      setSelectedUserId(null);
      setUserDraft(EMPTY_USER_DRAFT);
      setUserSearch('');
    } catch (error) {
      setUserMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido guardar el usuario.',
      );
    } finally {
      setIsSavingUser(false);
    }
  };

  const handleUserDelete = async () => {
    if (!selectedUserId) {
      setUserMessage('Selecciona un usuario antes de intentar eliminarlo.');
      return;
    }

    try {
      setIsSavingUser(true);
      setUserMessage(null);
      await deleteUser(selectedUserId);
      await loadDashboard();
      resetUserEditor();
      setUserMessage('Usuario eliminado correctamente.');
    } catch (error) {
      setUserMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido eliminar el usuario.',
      );
    } finally {
      setIsSavingUser(false);
    }
  };

  const handleClassSave = async () => {
    if (!classDraft.nombre.trim()) {
      setClassMessage('El nombre de la clase es obligatorio.');
      return;
    }

    const duration = Number(classDraft.duracion);

    if (!Number.isFinite(duration) || duration < 1 || duration > 300) {
      setClassMessage('La duracion debe estar entre 1 y 300 minutos.');
      return;
    }

    if (!classDraft.entrenadorId) {
      setClassMessage('Selecciona un entrenador para la clase.');
      return;
    }

    try {
      setIsSavingClass(true);
      setClassMessage(null);

      if (selectedClassId) {
        await updateGymClass(selectedClassId, classDraft);
        setClassMessage('Clase actualizada correctamente.');
      } else {
        await createGymClass(classDraft);
        setClassMessage('Clase creada correctamente.');
      }

      await loadDashboard();
      setSelectedClassId(null);
      setClassDraft(EMPTY_GYM_CLASS_DRAFT);
      setClassSearch('');
    } catch (error) {
      setClassMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido guardar la clase.',
      );
    } finally {
      setIsSavingClass(false);
    }
  };

  const handleClassDelete = async () => {
    if (!selectedClassId) {
      setClassMessage('Selecciona una clase antes de intentar eliminarla.');
      return;
    }

    try {
      setIsSavingClass(true);
      setClassMessage(null);
      await deleteGymClass(selectedClassId);
      await loadDashboard();
      resetClassEditor();
      setClassMessage('Clase eliminada correctamente.');
    } catch (error) {
      setClassMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido eliminar la clase.',
      );
    } finally {
      setIsSavingClass(false);
    }
  };

  const handleFeeSave = async () => {
    if (!feeDraft.nombre.trim()) {
      setFeeMessage('El nombre de la cuota es obligatorio.');
      return;
    }

    const price = Number(feeDraft.precio);

    if (!Number.isFinite(price) || price <= 0) {
      setFeeMessage('El precio debe ser un numero mayor que cero.');
      return;
    }

    try {
      setIsSavingFee(true);
      setFeeMessage(null);

      if (selectedFeeId) {
        await updateMembershipFee(selectedFeeId, feeDraft);
        setFeeMessage('Cuota actualizada correctamente.');
      } else {
        await createMembershipFee(feeDraft);
        setFeeMessage('Cuota creada correctamente.');
      }

      await loadDashboard();
      setSelectedFeeId(null);
      setFeeDraft(EMPTY_MEMBERSHIP_FEE_DRAFT);
      setFeeSearch('');
    } catch (error) {
      setFeeMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido guardar la cuota.',
      );
    } finally {
      setIsSavingFee(false);
    }
  };

  const handleFeeDelete = async () => {
    if (!selectedFeeId) {
      setFeeMessage('Selecciona una cuota antes de intentar eliminarla.');
      return;
    }

    try {
      setIsSavingFee(true);
      setFeeMessage(null);
      await deleteMembershipFee(selectedFeeId);
      await loadDashboard();
      resetFeeEditor();
      setFeeMessage('Cuota eliminada correctamente.');
    } catch (error) {
      setFeeMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido eliminar la cuota.',
      );
    } finally {
      setIsSavingFee(false);
    }
  };

  const handleQrSave = async () => {
    if (qrDraft.tipo === 'MACHINE' && !qrDraft.maquinaId) {
      setQrMessage('Selecciona una maquina para este QR.');
      return;
    }

    if (qrDraft.tipo === 'CLASS_SESSION' && !qrDraft.sesionClaseId) {
      setQrMessage('Selecciona una sesion de clase para este QR.');
      return;
    }

    try {
      setIsSavingQr(true);
      setQrMessage(null);

      if (selectedQrId) {
        await updateQrCode(selectedQrId, qrDraft);
        setQrMessage('QR actualizado correctamente.');
      } else {
        await createQrCode(qrDraft);
        setQrMessage('QR creado correctamente.');
      }

      await loadDashboard();
      setSelectedQrId(null);
      setQrDraft(EMPTY_QR_CODE_DRAFT);
      setQrSearch('');
    } catch (error) {
      setQrMessage(
        error instanceof Error ? error.message : 'No se ha podido guardar el QR.',
      );
    } finally {
      setIsSavingQr(false);
    }
  };

  const handleQrDelete = async () => {
    if (!selectedQrId) {
      setQrMessage('Selecciona un QR antes de intentar eliminarlo.');
      return;
    }

    try {
      setIsSavingQr(true);
      setQrMessage(null);
      await deleteQrCode(selectedQrId);
      await loadDashboard();
      resetQrEditor();
      setQrMessage('QR eliminado correctamente.');
    } catch (error) {
      setQrMessage(
        error instanceof Error ? error.message : 'No se ha podido eliminar el QR.',
      );
    } finally {
      setIsSavingQr(false);
    }
  };

  const renderDashboardSection = () => (
    <>
      <div className="grid grid-cols-1 gap-5 md:grid-cols-2 2xl:grid-cols-4">
        <AdminMetric
          label="Socios activos"
          value={isLoading ? '...' : String(activeClients)}
        />
        <AdminMetric
          label="Entrenadores"
          value={isLoading ? '...' : String(trainers)}
        />
        <AdminMetric
          label="Maquinas"
          value={isLoading ? '...' : String(machines)}
        />
        <AdminMetric
          label="Clases activas"
          value={isLoading ? '...' : String(activeClasses)}
        />
      </div>

      <div className="mt-8 grid grid-cols-1 gap-6 2xl:grid-cols-2">
        <AdminListCard
          description="Resumen rapido de los bloques principales del gimnasio."
          title="Gestion rapida"
        >
          {[
            [
              'Gestion de usuarios',
              data
                ? `${data.users.length} usuarios disponibles en el sistema`
                : 'Preparando el resumen de usuarios',
            ],
            [
              'Maquinas',
              data
                ? `${data.machines.length} maquinas cargadas desde la API`
                : 'Preparando el catalogo actual',
            ],
            [
              'Clases',
              data
                ? `${data.classes.length} clases visibles para administracion`
                : 'Preparando el listado de clases',
            ],
            [
              'Asistencias',
              data
                ? `${data.attendances.length} registros de actividad disponibles`
                : 'Preparando la actividad reciente',
            ],
          ].map(([title, subtitle]) => (
            <AdminInfoCard key={title} subtitle={subtitle} title={title} />
          ))}
        </AdminListCard>

        <AdminListCard
          description="Ultimas asistencias registradas en el sistema."
          title="Actividad reciente"
        >
          {isLoading ? (
            <AdminInfoCard
              subtitle="Estamos leyendo la actividad del backend."
              title="Cargando actividad"
            />
          ) : recentActivity.length > 0 ? (
            recentActivity.map((activity) => (
              <AdminInfoCard
                key={`${activity.title}-${activity.subtitle}`}
                subtitle={activity.subtitle}
                title={activity.title}
              />
            ))
          ) : (
            <AdminInfoCard
              subtitle="Todavia no hay asistencias registradas para mostrar aqui."
              title="Sin actividad reciente"
            />
          )}
        </AdminListCard>
      </div>
    </>
  );

  const renderUsersSection = () => (
    <div className="grid grid-cols-1 gap-6 2xl:grid-cols-[0.95fr_1.05fr]">
      <AdminListCard
        description="Busca, selecciona y revisa usuarios del sistema antes de editar."
        title="Usuarios del sistema"
      >
        <AdminTextField
          label="Buscar usuario"
          placeholder="Nombre, email, rol o ID"
          value={userSearch}
          onChange={setUserSearch}
        />

        {isLoading ? (
          <AdminInfoCard
            subtitle="Estamos cargando los usuarios registrados."
            title="Cargando usuarios"
          />
        ) : filteredUsers.length > 0 ? (
          filteredUsers.map((user) => (
            <button
              key={user.id}
              className={`w-full rounded-[20px] border px-5 py-4 text-left transition ${
                selectedUserId === user.id
                  ? 'border-[#22CC66] bg-[#F0FFF4]'
                  : 'border-[#EEF2F7] bg-[#F8FAFC]'
              }`}
              onClick={() => selectUser(user)}
              type="button"
            >
              <div className="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
                <div>
                  <p className="text-base font-semibold text-[#0D2010]">{user.nombre}</p>
                  <p className="mt-2 text-sm text-[#667085]">{user.email}</p>
                </div>
                <div className="flex flex-wrap gap-2">
                  <span className="rounded-full bg-white px-3 py-1 text-xs font-semibold text-[#166534]">
                    {userRoleLabel[user.role as UserDraft['roleName']] ?? user.role}
                  </span>
                  <span
                    className={`rounded-full px-3 py-1 text-xs font-semibold ${
                      user.activo
                        ? 'bg-[#DCFCE7] text-[#166534]'
                        : 'bg-[#F3F4F6] text-[#6B7280]'
                    }`}
                  >
                    {user.activo ? 'Activo' : 'Inactivo'}
                  </span>
                </div>
              </div>
            </button>
          ))
        ) : (
          <AdminInfoCard
            subtitle="No hay usuarios que coincidan con la busqueda actual."
            title="Sin resultados"
          />
        )}
      </AdminListCard>

      <AdminListCard
        description="Crea usuarios nuevos o actualiza los seleccionados desde la web."
        title={selectedUserId ? 'Editar usuario' : 'Nuevo usuario'}
      >
        <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div className="rounded-full bg-[#F0FFF4] px-4 py-2 text-sm font-semibold text-[#166534]">
            {selectedUserId ? 'Modo edicion' : 'Modo alta'}
          </div>
          <button
            className="rounded-full bg-[#F3F4F6] px-4 py-2 text-sm font-semibold text-[#374151]"
            onClick={resetUserEditor}
            type="button"
          >
            Limpiar
          </button>
        </div>

        <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
          <AdminTextField
            label="Nombre"
            placeholder="Laura Rodriguez"
            value={userDraft.nombre}
            onChange={(value) =>
              setUserDraft((current) => ({ ...current, nombre: value }))
            }
          />
          <AdminTextField
            label="Email"
            placeholder="laura@hazelgym.com"
            type="email"
            value={userDraft.email}
            onChange={(value) =>
              setUserDraft((current) => ({ ...current, email: value }))
            }
          />
          <AdminTextField
            label={selectedUserId ? 'Nueva contrasena (opcional)' : 'Contrasena inicial'}
            placeholder="Minimo 6 caracteres"
            type="password"
            value={userDraft.password}
            onChange={(value) =>
              setUserDraft((current) => ({ ...current, password: value }))
            }
          />
          <label className="block">
            <span className="mb-2 block text-sm font-semibold text-[#667085]">Rol</span>
            <select
              className="w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66]"
              value={userDraft.roleName}
              onChange={(event) =>
                setUserDraft((current) => ({
                  ...current,
                  roleName: event.target.value as UserDraft['roleName'],
                }))
              }
            >
              <option value="CLIENT">Cliente</option>
              <option value="TRAINER">Entrenador</option>
              <option value="ADMIN">Administrador</option>
            </select>
          </label>
          <label className="block">
            <span className="mb-2 block text-sm font-semibold text-[#667085]">Estado</span>
            <select
              className="w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66]"
              value={userDraft.activo ? 'activo' : 'inactivo'}
              onChange={(event) =>
                setUserDraft((current) => ({
                  ...current,
                  activo: event.target.value === 'activo',
                }))
              }
            >
              <option value="activo">Activo</option>
              <option value="inactivo">Inactivo</option>
            </select>
          </label>
        </div>

        {userMessage ? (
          <div className="rounded-[20px] border border-[#CFEAD6] bg-[#F0FFF4] px-4 py-3 text-sm text-[#166534]">
            {userMessage}
          </div>
        ) : null}

        <div className="flex flex-wrap gap-3">
          <button
            className="rounded-full bg-[#22CC66] px-5 py-3 text-sm font-semibold text-[#0D2010] disabled:cursor-not-allowed disabled:opacity-60"
            disabled={isSavingUser}
            onClick={() => void handleUserSave()}
            type="button"
          >
            {isSavingUser ? 'Guardando...' : 'Guardar usuario'}
          </button>
          {selectedUserId ? (
            <button
              className="rounded-full bg-[#0D2010] px-5 py-3 text-sm font-semibold text-white disabled:cursor-not-allowed disabled:opacity-60"
              disabled={isSavingUser}
              onClick={() => void handleUserDelete()}
              type="button"
            >
              Eliminar usuario
            </button>
          ) : null}
        </div>
      </AdminListCard>
    </div>
  );

  const renderQrSection = () => (
    <div className="space-y-6">
      <div className="grid grid-cols-1 gap-5 xl:grid-cols-4">
        {isLoading ? (
          <AdminInfoCard
            subtitle="Estamos preparando el resumen QR."
            title="Cargando"
          />
        ) : qrStats.length > 0 ? (
          qrStats.map((entry) => (
            <AdminMetric
              key={entry.type}
              label={`QR ${entry.type.toLowerCase()}`}
              value={String(entry.count)}
            />
          ))
        ) : (
          <AdminInfoCard
            subtitle="Todavia no hay actividad QR registrada."
            title="Sin datos QR"
          />
        )}
      </div>

      <div className="grid grid-cols-1 gap-6 2xl:grid-cols-[0.95fr_1.05fr]">
        <AdminListCard
          description="Busca un QR existente y revisa a que recurso esta vinculado."
          title="Codigos QR del sistema"
        >
          <AdminTextField
            label="Buscar QR"
            placeholder="Tipo, maquina, sesion o ID"
            value={qrSearch}
            onChange={setQrSearch}
          />

          {isLoading ? (
            <AdminInfoCard
              subtitle="Estamos cargando los codigos QR."
              title="Cargando QR"
            />
          ) : filteredQrCodes.length > 0 ? (
            filteredQrCodes.map((qrCode) => (
              <button
                key={qrCode.id}
                className={`w-full rounded-[20px] border px-5 py-4 text-left transition ${
                  selectedQrId === qrCode.id
                    ? 'border-[#22CC66] bg-[#F0FFF4]'
                    : 'border-[#EEF2F7] bg-[#F8FAFC]'
                }`}
                onClick={() => selectQrCode(qrCode)}
                type="button"
              >
                <div className="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
                  <div>
                    <p className="text-base font-semibold text-[#0D2010]">
                      QR #{qrCode.id} · {qrTypeLabel[qrCode.tipo]}
                    </p>
                    <p className="mt-2 text-sm text-[#667085]">
                      {qrCode.esEntradaGimnasio
                        ? 'Entrada del gimnasio'
                        : qrCode.maquinaNombre || qrCode.sesionClaseResumen || 'Sin vinculo visible'}
                    </p>
                  </div>
                  <span className="rounded-full bg-[#DCFCE7] px-3 py-1 text-xs font-semibold text-[#166534]">
                    {qrCode.tipo}
                  </span>
                </div>
              </button>
            ))
          ) : (
            <AdminInfoCard
              subtitle="No hay codigos QR que coincidan con la busqueda actual."
              title="Sin resultados"
            />
          )}
        </AdminListCard>

        <AdminListCard
          description="La web se centra en gestionar y supervisar los QR; el escaneo principal queda en la app movil."
          title={selectedQrId ? 'Editar QR' : 'Nuevo QR'}
        >
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <div className="rounded-full bg-[#F0FFF4] px-4 py-2 text-sm font-semibold text-[#166534]">
              {selectedQrId ? 'Modo edicion' : 'Modo alta'}
            </div>
            <button
              className="rounded-full bg-[#F3F4F6] px-4 py-2 text-sm font-semibold text-[#374151]"
              onClick={resetQrEditor}
              type="button"
            >
              Limpiar
            </button>
          </div>

          <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
            <label className="block">
              <span className="mb-2 block text-sm font-semibold text-[#667085]">Tipo</span>
              <select
                className="w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66]"
                value={qrDraft.tipo}
                onChange={(event) =>
                  setQrDraft((current) => ({
                    ...current,
                    tipo: event.target.value as QrCodeDraft['tipo'],
                    esEntradaGimnasio: event.target.value === 'ENTRY',
                    maquinaId: event.target.value === 'MACHINE' ? current.maquinaId : '',
                    sesionClaseId:
                      event.target.value === 'CLASS_SESSION'
                        ? current.sesionClaseId
                        : '',
                  }))
                }
              >
                <option value="ENTRY">Entrada</option>
                <option value="MACHINE">Maquina</option>
                <option value="CLASS_SESSION">Sesion de clase</option>
              </select>
            </label>

            <label className="block">
              <span className="mb-2 block text-sm font-semibold text-[#667085]">
                Entrada de gimnasio
              </span>
              <select
                className="w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66]"
                value={qrDraft.esEntradaGimnasio ? 'si' : 'no'}
                onChange={(event) =>
                  setQrDraft((current) => ({
                    ...current,
                    esEntradaGimnasio: event.target.value === 'si',
                  }))
                }
              >
                <option value="si">Si</option>
                <option value="no">No</option>
              </select>
            </label>

            {qrDraft.tipo === 'MACHINE' ? (
              <label className="block lg:col-span-2">
                <span className="mb-2 block text-sm font-semibold text-[#667085]">
                  Maquina vinculada
                </span>
                <select
                  className="w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66]"
                  value={qrDraft.maquinaId}
                  onChange={(event) =>
                    setQrDraft((current) => ({
                      ...current,
                      maquinaId: event.target.value,
                    }))
                  }
                >
                  <option value="">Selecciona una maquina</option>
                  {data?.machines.map((machine) => (
                    <option key={machine.id} value={machine.id}>
                      {machine.nombre} · ID {machine.id}
                    </option>
                  ))}
                </select>
              </label>
            ) : null}

            {qrDraft.tipo === 'CLASS_SESSION' ? (
              <label className="block lg:col-span-2">
                <span className="mb-2 block text-sm font-semibold text-[#667085]">
                  Sesion vinculada
                </span>
                <select
                  className="w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66]"
                  value={qrDraft.sesionClaseId}
                  onChange={(event) =>
                    setQrDraft((current) => ({
                      ...current,
                      sesionClaseId: event.target.value,
                    }))
                  }
                >
                  <option value="">Selecciona una sesion de clase</option>
                  {data?.classSessions.map((session: ClassSessionSummary) => (
                    <option key={session.id} value={session.id}>
                      {session.gymClassName} · {session.fecha} · {session.horaInicio}
                    </option>
                  ))}
                </select>
              </label>
            ) : null}
          </div>

          <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
            <AdminInfoCard
              subtitle="Registro rapido de asistencia y escaneo con camara del telefono."
              title="Movil: escaneo principal"
            />
            <AdminInfoCard
              subtitle="Seguimiento de actividad, control de recursos y organizacion interna."
              title="Web: supervision y gestion"
            />
          </div>

          {qrMessage ? (
            <div className="rounded-[20px] border border-[#CFEAD6] bg-[#F0FFF4] px-4 py-3 text-sm text-[#166534]">
              {qrMessage}
            </div>
          ) : null}

          <div className="flex flex-wrap gap-3">
            <button
              className="rounded-full bg-[#22CC66] px-5 py-3 text-sm font-semibold text-[#0D2010] disabled:cursor-not-allowed disabled:opacity-60"
              disabled={isSavingQr}
              onClick={() => void handleQrSave()}
              type="button"
            >
              {isSavingQr
                ? 'Guardando...'
                : selectedQrId
                  ? 'Guardar cambios'
                  : 'Crear QR'}
            </button>
            {selectedQrId ? (
              <button
                className="rounded-full bg-[#0D2010] px-5 py-3 text-sm font-semibold text-white disabled:cursor-not-allowed disabled:opacity-60"
                disabled={isSavingQr}
                onClick={() => void handleQrDelete()}
                type="button"
              >
                Eliminar QR
              </button>
            ) : null}
          </div>
        </AdminListCard>
      </div>
    </div>
  );

  const renderMachinesSection = () => (
    <div className="grid grid-cols-1 gap-6 2xl:grid-cols-[0.95fr_1.05fr]">
      <AdminListCard
        description="Selecciona una maquina para editarla o crea una nueva."
        title="Catalogo de maquinas"
      >
        {isLoading ? (
          <AdminInfoCard
            subtitle="Estamos cargando el catalogo."
            title="Cargando maquinas"
          />
        ) : data && data.machines.length > 0 ? (
          data.machines.map((machine) => (
            <button
              key={machine.id}
              className={`w-full rounded-[20px] border px-5 py-4 text-left transition ${
                selectedMachineId === machine.id
                  ? 'border-[#22CC66] bg-[#F0FFF4]'
                  : 'border-[#EEF2F7] bg-[#F8FAFC]'
              }`}
              onClick={() => selectMachine(machine)}
              type="button"
            >
              <p className="text-base font-semibold text-[#0D2010]">{machine.nombre}</p>
              <p className="mt-2 text-sm text-[#667085]">
                {machine.grupoMuscular || 'Sin grupo muscular'} - {machine.estado}
              </p>
            </button>
          ))
        ) : (
          <AdminInfoCard
            subtitle="No hay maquinas registradas en este momento."
            title="Sin maquinas"
          />
        )}
      </AdminListCard>

      <section className="rounded-[28px] border border-[#E7EAEE] bg-white p-7">
        <div className="mb-6 flex items-start justify-between gap-4">
          <div>
            <h4 className="font-['Syne'] text-2xl font-bold text-[#0D2010]">
              {selectedMachineId ? 'Editar maquina' : 'Nueva maquina'}
            </h4>
            <p className="mt-2 text-sm text-[#98A2B3]">
              Formulario sencillo para alta, edicion y borrado desde la web.
            </p>
          </div>
          <button
            className="rounded-full bg-[#F3F4F6] px-4 py-2 text-sm font-semibold text-[#374151]"
            onClick={resetMachineEditor}
            type="button"
          >
            Limpiar
          </button>
        </div>

        <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
          <AdminTextField
            label="Nombre"
            placeholder="Press de banca"
            value={machineDraft.nombre}
            onChange={(value) =>
              setMachineDraft((current) => ({ ...current, nombre: value }))
            }
          />
          <AdminTextField
            label="Grupo muscular"
            placeholder="Pectoral, hombro"
            value={machineDraft.grupoMuscular}
            onChange={(value) =>
              setMachineDraft((current) => ({ ...current, grupoMuscular: value }))
            }
          />
          <AdminTextField
            label="Descripcion"
            placeholder="Maquina guiada para trabajo principal"
            value={machineDraft.descripcion}
            onChange={(value) =>
              setMachineDraft((current) => ({ ...current, descripcion: value }))
            }
          />
          <AdminTextField
            label="Nivel"
            placeholder="Inicial, medio o avanzado"
            value={machineDraft.nivel}
            onChange={(value) =>
              setMachineDraft((current) => ({ ...current, nivel: value }))
            }
          />
          <AdminTextField
            label="Instrucciones"
            placeholder="Indicaciones de uso"
            value={machineDraft.instrucciones}
            onChange={(value) =>
              setMachineDraft((current) => ({ ...current, instrucciones: value }))
            }
          />
          <AdminTextField
            label="Advertencia"
            placeholder="Consejo de seguridad"
            value={machineDraft.advertenciaSeguridad}
            onChange={(value) =>
              setMachineDraft((current) => ({
                ...current,
                advertenciaSeguridad: value,
              }))
            }
          />
          <AdminTextField
            label="Imagen URL"
            placeholder="https://..."
            value={machineDraft.imagenUrl}
            onChange={(value) =>
              setMachineDraft((current) => ({ ...current, imagenUrl: value }))
            }
          />
          <label className="block">
            <span className="mb-2 block text-sm font-semibold text-[#667085]">
              Estado
            </span>
            <select
              className="w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66]"
              value={machineDraft.estado}
              onChange={(event) =>
                setMachineDraft((current) => ({
                  ...current,
                  estado: event.target.value,
                }))
              }
            >
              <option value="ACTIVA">ACTIVA</option>
              <option value="FUERA_DE_SERVICIO">FUERA_DE_SERVICIO</option>
            </select>
          </label>
        </div>

        {machineMessage ? (
          <div className="mt-5 rounded-2xl border border-[#D1FADF] bg-[#F0FDF4] px-4 py-3 text-sm text-[#166534]">
            {machineMessage}
          </div>
        ) : null}

        <div className="mt-6 flex flex-wrap gap-3">
          <button
            className="rounded-full bg-[#22CC66] px-5 py-3 text-sm font-semibold text-[#0D2010] disabled:cursor-not-allowed disabled:opacity-60"
            disabled={isSavingMachine}
            onClick={handleMachineSave}
            type="button"
          >
            {isSavingMachine
              ? 'Guardando...'
              : selectedMachineId
                ? 'Guardar cambios'
                : 'Crear maquina'}
          </button>
          <button
            className="rounded-full bg-[#0D2010] px-5 py-3 text-sm font-semibold text-white disabled:cursor-not-allowed disabled:opacity-60"
            disabled={isSavingMachine || !selectedMachineId}
            onClick={handleMachineDelete}
            type="button"
          >
            Eliminar maquina
          </button>
        </div>
      </section>
    </div>
  );

  const renderClassesSection = () => (
    <div className="grid grid-cols-1 gap-6 2xl:grid-cols-[0.95fr_1.05fr]">
      <AdminListCard
        description="Busca una clase existente o revisa rapidamente quien la imparte."
        title="Clases del gimnasio"
      >
        <AdminTextField
          label="Buscar clase"
          placeholder="Nombre, descripcion, entrenador o ID"
          value={classSearch}
          onChange={setClassSearch}
        />

        {isLoading ? (
          <AdminInfoCard
            subtitle="Estamos cargando las clases activas e inactivas."
            title="Cargando clases"
          />
        ) : filteredClasses.length > 0 ? (
          filteredClasses.map((gymClass: GymClassSummary) => (
            <button
              key={gymClass.id}
              className={`w-full rounded-[20px] border px-5 py-4 text-left transition ${
                selectedClassId === gymClass.id
                  ? 'border-[#22CC66] bg-[#F0FFF4]'
                  : 'border-[#EEF2F7] bg-[#F8FAFC]'
              }`}
              onClick={() => selectClass(gymClass)}
              type="button"
            >
              <div className="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
                <div>
                  <p className="text-base font-semibold text-[#0D2010]">{gymClass.nombre}</p>
                  <p className="mt-2 text-sm text-[#667085]">
                    {gymClass.entrenadorNombre || 'Sin entrenador'} -{' '}
                    {gymClass.duracion ? `${gymClass.duracion} min` : 'Sin duracion'}
                  </p>
                </div>
                <span
                  className={`rounded-full px-3 py-1 text-xs font-semibold ${
                    gymClass.activa
                      ? 'bg-[#DCFCE7] text-[#166534]'
                      : 'bg-[#F3F4F6] text-[#6B7280]'
                  }`}
                >
                  {gymClass.activa ? 'Activa' : 'Inactiva'}
                </span>
              </div>
            </button>
          ))
        ) : (
          <AdminInfoCard
            subtitle="No hay clases que coincidan con la busqueda actual."
            title="Sin resultados"
          />
        )}
      </AdminListCard>

      <AdminListCard
        description="Crea nuevas clases o modifica las ya existentes desde administracion."
        title={selectedClassId ? 'Editar clase' : 'Nueva clase'}
      >
        <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div className="rounded-full bg-[#F0FFF4] px-4 py-2 text-sm font-semibold text-[#166534]">
            {selectedClassId ? 'Modo edicion' : 'Modo alta'}
          </div>
          <button
            className="rounded-full bg-[#F3F4F6] px-4 py-2 text-sm font-semibold text-[#374151]"
            onClick={resetClassEditor}
            type="button"
          >
            Limpiar
          </button>
        </div>

        <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
          <AdminTextField
            label="Nombre"
            placeholder="Spinning, Yoga o HIIT"
            value={classDraft.nombre}
            onChange={(value) =>
              setClassDraft((current) => ({ ...current, nombre: value }))
            }
          />
          <AdminTextField
            label="Duracion"
            placeholder="60"
            value={classDraft.duracion}
            onChange={(value) =>
              setClassDraft((current) => ({ ...current, duracion: value }))
            }
          />
          <label className="block lg:col-span-2">
            <span className="mb-2 block text-sm font-semibold text-[#667085]">
              Descripcion
            </span>
            <textarea
              className="min-h-[120px] w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66]"
              placeholder="Objetivo, nivel o enfoque general de la clase"
              value={classDraft.descripcion}
              onChange={(event) =>
                setClassDraft((current) => ({
                  ...current,
                  descripcion: event.target.value,
                }))
              }
            />
          </label>
          <label className="block">
            <span className="mb-2 block text-sm font-semibold text-[#667085]">
              Entrenador
            </span>
            <select
              className="w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66]"
              value={classDraft.entrenadorId}
              onChange={(event) =>
                setClassDraft((current) => ({
                  ...current,
                  entrenadorId: event.target.value,
                }))
              }
            >
              <option value="">Selecciona un entrenador</option>
              {trainersList.map((trainer) => (
                <option key={trainer.id} value={trainer.id}>
                  {trainer.nombre} · ID {trainer.id}
                </option>
              ))}
            </select>
          </label>
          <label className="block">
            <span className="mb-2 block text-sm font-semibold text-[#667085]">Estado</span>
            <select
              className="w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66]"
              value={classDraft.activa ? 'activa' : 'inactiva'}
              onChange={(event) =>
                setClassDraft((current) => ({
                  ...current,
                  activa: event.target.value === 'activa',
                }))
              }
            >
              <option value="activa">Activa</option>
              <option value="inactiva">Inactiva</option>
            </select>
          </label>
        </div>

        {classMessage ? (
          <div className="rounded-[20px] border border-[#CFEAD6] bg-[#F0FFF4] px-4 py-3 text-sm text-[#166534]">
            {classMessage}
          </div>
        ) : null}

        <div className="flex flex-wrap gap-3">
          <button
            className="rounded-full bg-[#22CC66] px-5 py-3 text-sm font-semibold text-[#0D2010] disabled:cursor-not-allowed disabled:opacity-60"
            disabled={isSavingClass}
            onClick={() => void handleClassSave()}
            type="button"
          >
            {isSavingClass
              ? 'Guardando...'
              : selectedClassId
                ? 'Guardar cambios'
                : 'Crear clase'}
          </button>
          {selectedClassId ? (
            <button
              className="rounded-full bg-[#0D2010] px-5 py-3 text-sm font-semibold text-white disabled:cursor-not-allowed disabled:opacity-60"
              disabled={isSavingClass}
              onClick={() => void handleClassDelete()}
              type="button"
            >
              Eliminar clase
            </button>
          ) : null}
        </div>
      </AdminListCard>
    </div>
  );

  const renderFeesSection = () => (
    <div className="grid grid-cols-1 gap-6 2xl:grid-cols-[0.95fr_1.05fr]">
      <AdminListCard
        description="Busca una cuota existente y revisa rapidamente su precio y descripcion."
        title="Cuotas del gimnasio"
      >
        <AdminTextField
          label="Buscar cuota"
          placeholder="Nombre, descripcion, precio o ID"
          value={feeSearch}
          onChange={setFeeSearch}
        />

        {isLoading ? (
          <AdminInfoCard
            subtitle="Estamos cargando las cuotas del backend."
            title="Cargando cuotas"
          />
        ) : filteredFees.length > 0 ? (
          filteredFees.map((fee: MembershipFeeSummary) => (
            <button
              key={fee.id}
              className={`w-full rounded-[20px] border px-5 py-4 text-left transition ${
                selectedFeeId === fee.id
                  ? 'border-[#22CC66] bg-[#F0FFF4]'
                  : 'border-[#EEF2F7] bg-[#F8FAFC]'
              }`}
              onClick={() => selectFee(fee)}
              type="button"
            >
              <div className="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
                <div>
                  <p className="text-base font-semibold text-[#0D2010]">{fee.nombre}</p>
                  <p className="mt-2 text-sm text-[#667085]">
                    {fee.descripcion || 'Sin descripcion'}
                  </p>
                </div>
                <span className="rounded-full bg-[#DCFCE7] px-3 py-1 text-xs font-semibold text-[#166534]">
                  {formatPrice(fee.precio)}
                </span>
              </div>
            </button>
          ))
        ) : (
          <AdminInfoCard
            subtitle="No hay cuotas que coincidan con la busqueda actual."
            title="Sin resultados"
          />
        )}
      </AdminListCard>

      <AdminListCard
        description="Crea nuevas cuotas o actualiza las que ya existen desde la web."
        title={selectedFeeId ? 'Editar cuota' : 'Nueva cuota'}
      >
        <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div className="rounded-full bg-[#F0FFF4] px-4 py-2 text-sm font-semibold text-[#166534]">
            {selectedFeeId ? 'Modo edicion' : 'Modo alta'}
          </div>
          <button
            className="rounded-full bg-[#F3F4F6] px-4 py-2 text-sm font-semibold text-[#374151]"
            onClick={resetFeeEditor}
            type="button"
          >
            Limpiar
          </button>
        </div>

        <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
          <AdminTextField
            label="Nombre"
            placeholder="Mensual, trimestral o premium"
            value={feeDraft.nombre}
            onChange={(value) =>
              setFeeDraft((current) => ({ ...current, nombre: value }))
            }
          />
          <AdminTextField
            label="Precio"
            placeholder="39.99"
            value={feeDraft.precio}
            onChange={(value) =>
              setFeeDraft((current) => ({ ...current, precio: value }))
            }
          />
          <label className="block lg:col-span-2">
            <span className="mb-2 block text-sm font-semibold text-[#667085]">
              Descripcion
            </span>
            <textarea
              className="min-h-[140px] w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66]"
              placeholder="Que incluye la cuota, duracion o condiciones principales"
              value={feeDraft.descripcion}
              onChange={(event) =>
                setFeeDraft((current) => ({
                  ...current,
                  descripcion: event.target.value,
                }))
              }
            />
          </label>
        </div>

        {feeMessage ? (
          <div className="rounded-[20px] border border-[#CFEAD6] bg-[#F0FFF4] px-4 py-3 text-sm text-[#166534]">
            {feeMessage}
          </div>
        ) : null}

        <div className="flex flex-wrap gap-3">
          <button
            className="rounded-full bg-[#22CC66] px-5 py-3 text-sm font-semibold text-[#0D2010] disabled:cursor-not-allowed disabled:opacity-60"
            disabled={isSavingFee}
            onClick={() => void handleFeeSave()}
            type="button"
          >
            {isSavingFee
              ? 'Guardando...'
              : selectedFeeId
                ? 'Guardar cambios'
                : 'Crear cuota'}
          </button>
          {selectedFeeId ? (
            <button
              className="rounded-full bg-[#0D2010] px-5 py-3 text-sm font-semibold text-white disabled:cursor-not-allowed disabled:opacity-60"
              disabled={isSavingFee}
              onClick={() => void handleFeeDelete()}
              type="button"
            >
              Eliminar cuota
            </button>
          ) : null}
        </div>
      </AdminListCard>
    </div>
  );

  const renderSection = () => {
    switch (activeSection) {
      case 'usuarios':
        return renderUsersSection();
      case 'qr':
        return renderQrSection();
      case 'maquinas':
        return renderMachinesSection();
      case 'clases':
        return renderClassesSection();
      case 'cuotas':
        return renderFeesSection();
      case 'dashboard':
      default:
        return renderDashboardSection();
    }
  };

  const sectionTitles: Record<AdminSection, string> = {
    clases: 'Clases',
    cuotas: 'Cuotas',
    dashboard: 'Dashboard',
    maquinas: 'Maquinas',
    qr: 'QR',
    usuarios: 'Usuarios',
  };

  return (
    <div className="min-h-screen w-full bg-[radial-gradient(circle_at_top_left,_rgba(34,204,102,0.10),_transparent_18%),linear-gradient(180deg,#F2F5F2_0%,#EDF4EE_100%)] text-[#0D2010]">
      <div className="grid min-h-screen grid-cols-1 lg:grid-cols-[240px_minmax(0,1fr)]">
        <aside className="border-b border-[#1F4D2F] bg-[linear-gradient(180deg,#0D2010_0%,#12311A_100%)] px-4 py-6 sm:px-5 sm:py-7 lg:sticky lg:top-0 lg:flex lg:h-screen lg:flex-col lg:justify-between lg:border-r lg:border-b-0">
          <div>
          <h2 className="font-['Syne'] text-3xl font-extrabold text-white">
            Hazel Gym
          </h2>
          <p className="mt-2 text-sm text-[#A7F3D0]">Administracion</p>
          <div className="mt-8 grid grid-cols-2 gap-3 sm:grid-cols-3 lg:mt-10 lg:grid-cols-1">
            <AdminSidebarItem
              active={activeSection === 'dashboard'}
              label="Dashboard"
              onClick={() => setActiveSection('dashboard')}
            />
            <AdminSidebarItem
              active={activeSection === 'usuarios'}
              label="Usuarios"
              onClick={() => setActiveSection('usuarios')}
            />
            <AdminSidebarItem
              active={activeSection === 'qr'}
              label="QR"
              onClick={() => setActiveSection('qr')}
            />
            <AdminSidebarItem
              active={activeSection === 'maquinas'}
              label="Maquinas"
              onClick={() => setActiveSection('maquinas')}
            />
            <AdminSidebarItem
              active={activeSection === 'clases'}
              label="Clases"
              onClick={() => setActiveSection('clases')}
            />
            <AdminSidebarItem
              active={activeSection === 'cuotas'}
              label="Cuotas"
              onClick={() => setActiveSection('cuotas')}
            />
          </div>
          </div>
          <div className="mt-8 hidden rounded-[22px] border border-[#1F4D2F] bg-[#102818] p-4 text-sm text-[#A7F3D0] lg:block">
            Gestion centralizada para usuarios, clases, cuotas, QR y maquinas.
          </div>
        </aside>

        <main className="min-w-0 px-4 py-6 sm:px-6 lg:px-8 xl:p-10">
          <div className="mb-8 rounded-[30px] border border-white/70 bg-white/74 p-5 shadow-[0_24px_55px_rgba(16,24,40,0.08)] backdrop-blur-xl sm:p-6 xl:p-7">
          <div className="flex flex-col gap-4 xl:flex-row xl:items-start xl:justify-between">
            <div>
              <p className="text-sm font-medium uppercase tracking-[0.2em] text-[#667085]">
                Panel de administracion
              </p>
              <h3 className="font-['Syne'] text-3xl font-extrabold text-[#0D2010] sm:text-4xl xl:text-5xl">
                {userName}
              </h3>
              <p className="mt-3 text-sm text-[#98A2B3]">
                Seccion actual: {sectionTitles[activeSection]}
              </p>
            </div>
            <div className="flex flex-wrap items-center gap-3">
              <div className="rounded-full bg-[#DCFCE7] px-5 py-3 text-sm font-semibold text-[#166534]">
                Administrador
              </div>
              <button
                className="rounded-full bg-[#0D2010] px-5 py-3 text-sm font-semibold text-white"
                onClick={onLogout}
              >
                Cerrar sesion
              </button>
            </div>
          </div>
          </div>

          {errorMessage ? (
            <div className="mb-6 rounded-[22px] border border-[#FFB4A3] bg-[#FFF3EF] px-5 py-4 text-sm text-[#B93815]">
              {errorMessage}
            </div>
          ) : null}

          <div className="space-y-8">{renderSection()}</div>
        </main>
      </div>
    </div>
  );
}
