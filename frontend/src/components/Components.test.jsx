import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import { afterEach, beforeEach, expect, test, vi } from 'vitest';
import AppointmentForm from './AppointmentForm';
import CitasPublic from './CitasPublic';
import MicroservicesPanel from './MicroservicesPanel';
import PatientDetail from './PatientDetail';
import PatientForm from './PatientForm';
import PatientList from './PatientList';
import ReassignmentPanel from './ReassignmentPanel';
import SettingsPanel from './SettingsPanel';
import * as api from '../hooks/usePatientApi';

vi.mock('../hooks/usePatientApi');

const patients = [
  { id: 1, nombre: 'Ana', apellido: 'Lopez', rut: '11111111-1', historialClinico: 'Control' }
];

beforeEach(() => {
  vi.clearAllMocks();
  api.fetchPatients.mockResolvedValue(patients);
  api.fetchAllCitas.mockResolvedValue([{ id: 1, pacienteId: 1, fecha: '2026-06-20', hora: '10:00' }]);
  api.fetchListaEsperaPendiente.mockResolvedValue([
    { id: 10, pacienteId: 1, especialidad: 'CARDIOLOGIA', prioridad: 'ALTA', observaciones: 'Urgente' }
  ]);
  api.fetchReassignments.mockResolvedValue([]);
  api.fetchAuditEvents.mockResolvedValue([
    { id: 30, eventType: 'PACIENTE_CREADO', occurredAt: '2026-06-18T12:00:00Z', payload: '{}' }
  ]);
  api.fetchNotifications.mockResolvedValue([
    { id: 40, title: 'Nuevo paciente', eventType: 'PACIENTE_CREADO', message: 'Registrado', readFlag: false }
  ]);
  api.markNotificationAsRead.mockResolvedValue({ id: 40, readFlag: true });
  api.createCita.mockResolvedValue({ id: 99 });
  api.registerDoctor.mockResolvedValue({
    token: 'jwt',
    name: 'Dra. Ana',
    email: 'ana@redsalud.cl',
    role: 'medico',
    registeredUsers: 2
  });
  api.createListaEspera.mockResolvedValue({ id: 11 });
  api.reassignAppointment.mockResolvedValue({ id: 21 });
  api.updateListaEsperaEstado.mockResolvedValue({ id: 10 });
  window.alert = vi.fn();
});

afterEach(() => {
  cleanup();
});

test('PatientList muestra pacientes y permite seleccionar', () => {
  const onSelect = vi.fn();
  render(<PatientList patients={patients} onSelect={onSelect} selectedId={1} />);

  fireEvent.click(screen.getByText(/Ana Lopez/i));
  expect(onSelect).toHaveBeenCalledWith(1);
});

test('PatientList muestra estado vacio', () => {
  render(<PatientList patients={[]} onSelect={vi.fn()} />);
  expect(screen.getByText(/No hay pacientes disponibles/i)).toBeTruthy();
});

test('PatientDetail muestra ficha y citas', () => {
  render(
    <PatientDetail
      patient={patients[0]}
      citas={[{ id: 2, pacienteId: 1, fecha: '2026-06-21', hora: '' }]}
    />
  );

  expect(screen.getByText(/Historial clinico/i)).toBeTruthy();
  expect(screen.getByText(/Hora por confirmar/i)).toBeTruthy();
});

test('PatientForm envia y limpia formulario', async () => {
  const onSubmit = vi.fn().mockResolvedValue();
  render(<PatientForm onSubmit={onSubmit} saving={false} />);

  fireEvent.change(screen.getByPlaceholderText('12345678-9'), { target: { value: '11111111-1' } });
  fireEvent.change(screen.getByPlaceholderText('Nombre'), { target: { value: 'Ana' } });
  fireEvent.change(screen.getByPlaceholderText('Apellido'), { target: { value: 'Lopez' } });
  fireEvent.click(screen.getByRole('button', { name: /Agregar paciente/i }));

  await waitFor(() => expect(onSubmit).toHaveBeenCalledWith(expect.objectContaining({ nombre: 'Ana' })));
});

test('CitasPublic lista citas disponibles', async () => {
  render(<CitasPublic />);
  await waitFor(() => expect(screen.getByText(/2026-06-20/)).toBeTruthy());
});

test('AppointmentForm crea una cita', async () => {
  render(<AppointmentForm onAppointmentCreated={vi.fn()} />);

  await waitFor(() => expect(screen.getByRole('option', { name: /Ana Lopez/i })).toBeTruthy());
  fireEvent.change(screen.getByLabelText(/Paciente/i), { target: { value: '1' } });
  fireEvent.change(screen.getByLabelText(/Especialidad/i), { target: { value: 'CARDIOLOGIA' } });
  fireEvent.change(screen.getByLabelText(/Fecha/i), { target: { value: '2026-06-20' } });
  fireEvent.change(screen.getByLabelText(/Hora/i), { target: { value: '10:00' } });
  fireEvent.change(screen.getByLabelText(/Motivo/i), { target: { value: 'Control' } });
  fireEvent.click(screen.getByRole('button', { name: /Agendar Cita/i }));

  await waitFor(() => expect(api.createCita).toHaveBeenCalledWith(expect.objectContaining({ pacienteId: 1 })));
});

test('MicroservicesPanel carga datos y ejecuta acciones', async () => {
  render(<MicroservicesPanel />);

  await waitFor(() => expect(screen.getByText(/Cola priorizada/i)).toBeTruthy());
  fireEvent.click(screen.getByRole('button', { name: /Atender/i }));
  fireEvent.click(screen.getByRole('button', { name: /Marcar leida/i }));

  await waitFor(() => expect(api.updateListaEsperaEstado).toHaveBeenCalledWith(10, 'atender'));
  expect(api.markNotificationAsRead).toHaveBeenCalledWith(40);
});

test('ReassignmentPanel registra una nueva hora', async () => {
  render(<ReassignmentPanel doctorName="Dra. Ana" />);

  await waitFor(() => expect(screen.getByRole('option', { name: /Paciente 1/i })).toBeTruthy());
  fireEvent.change(screen.getByLabelText(/Cita a modificar/i), { target: { value: '1' } });
  fireEvent.change(screen.getByLabelText(/Nueva fecha/i), { target: { value: '2026-07-01' } });
  fireEvent.change(screen.getByLabelText(/Nueva hora/i), { target: { value: '12:30' } });
  fireEvent.change(screen.getByLabelText(/Motivo de reprogramacion/i), {
    target: { value: 'Paciente atrasado' }
  });
  fireEvent.click(screen.getByRole('button', { name: /Confirmar nuevo horario/i }));

  await waitFor(() => expect(api.reassignAppointment).toHaveBeenCalledWith(expect.objectContaining({
    citaId: 1,
    medicoResponsable: 'Dra. Ana'
  })));
});

test('SettingsPanel cambia preferencias funcionales', () => {
  const onChange = vi.fn();
  render(
    <SettingsPanel
      settings={{ compactMode: false, showNotifications: true, autoRefresh: false, highContrast: false }}
      onChange={onChange}
    />
  );

  fireEvent.click(screen.getByText(/Vista compacta/i));
  expect(onChange).toHaveBeenCalledWith(expect.objectContaining({ compactMode: true }));
});
