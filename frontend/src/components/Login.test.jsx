import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import { afterEach, beforeEach, expect, test, vi } from 'vitest';
import Login from './Login';
import * as api from '../hooks/usePatientApi';

vi.mock('../hooks/usePatientApi', () => ({
  loginDoctor: vi.fn(),
  registerDoctor: vi.fn()
}));

beforeEach(() => {
  vi.clearAllMocks();
  api.loginDoctor.mockResolvedValue({
    token: 'jwt',
    name: 'Dra. Ana',
    email: 'ana@redsalud.cl',
    role: 'medico',
    registeredUsers: 3
  });
  api.registerDoctor.mockResolvedValue({
    token: 'jwt',
    name: 'Dra. Ana',
    email: 'ana@redsalud.cl',
    role: 'medico',
    registeredUsers: 4
  });
});

afterEach(() => cleanup());

test('valida dominio, autentica y muestra errores del BFF', async () => {
  const onLogin = vi.fn();
  const { rerender } = render(<Login onLogin={onLogin} error="" />);

  fireEvent.change(screen.getByLabelText(/Correo institucional/i), {
    target: { value: 'externo@gmail.com' }
  });
  fireEvent.change(screen.getByLabelText(/Contrasena/i), {
    target: { value: 'segura123' }
  });
  fireEvent.click(screen.getByRole('button', { name: /Entrar al panel/i }));
  expect(screen.getByText(/Usa tu correo institucional/i)).toBeTruthy();

  fireEvent.change(screen.getByLabelText(/Correo institucional/i), {
    target: { value: 'ANA@REDSALUD.CL' }
  });
  fireEvent.click(screen.getByRole('button', { name: /Entrar al panel/i }));
  await waitFor(() => expect(onLogin).toHaveBeenCalledWith(expect.objectContaining({
    email: 'ana@redsalud.cl',
    token: 'jwt'
  })));

  api.loginDoctor.mockRejectedValueOnce(new Error('401'));
  fireEvent.click(screen.getByRole('button', { name: /Entrar al panel/i }));
  await waitFor(() => expect(screen.getByText(/Cuenta no encontrada/i)).toBeTruthy());

  rerender(<Login onLogin={onLogin} error="Error externo" />);
});

test('valida y registra una cuenta institucional', async () => {
  render(<Login onLogin={vi.fn()} error="" />);
  fireEvent.click(screen.getByRole('button', { name: /Registrarse/i }));

  const name = screen.getByLabelText(/Nombre completo/i);
  const email = screen.getByLabelText(/Correo institucional/i);
  const password = screen.getByLabelText(/^Contrasena$/i);
  const confirm = screen.getByLabelText(/Confirmar/i);
  const submit = screen.getByRole('button', { name: /Registrar medico/i });

  fireEvent.change(name, { target: { value: ' ' } });
  fireEvent.change(email, { target: { value: 'ana@redsalud.cl' } });
  fireEvent.change(password, { target: { value: 'segura123' } });
  fireEvent.change(confirm, { target: { value: 'segura123' } });
  fireEvent.click(submit);
  expect(screen.getByText(/Ingresa el nombre/i)).toBeTruthy();

  fireEvent.change(name, { target: { value: 'Dra. Ana' } });
  fireEvent.change(email, { target: { value: 'ana@gmail.com' } });
  fireEvent.click(submit);
  expect(screen.getByText(/Solo se permite registrar/i)).toBeTruthy();

  fireEvent.change(email, { target: { value: 'ana@redsalud.cl' } });
  fireEvent.change(password, { target: { value: 'corta' } });
  fireEvent.change(confirm, { target: { value: 'corta' } });
  fireEvent.click(submit);
  expect(screen.getByText(/al menos 8 caracteres/i)).toBeTruthy();

  fireEvent.change(password, { target: { value: 'segura123' } });
  fireEvent.change(confirm, { target: { value: 'distinta123' } });
  fireEvent.click(submit);
  expect(screen.getByText(/no coinciden/i)).toBeTruthy();

  fireEvent.change(confirm, { target: { value: 'segura123' } });
  fireEvent.click(submit);
  await waitFor(() => expect(api.registerDoctor).toHaveBeenCalled());
  expect(screen.getByText(/Registro protegido creado/i)).toBeTruthy();
});

test('informa error cuando el registro falla', async () => {
  api.registerDoctor.mockRejectedValueOnce(new Error('duplicado'));
  render(<Login onLogin={vi.fn()} error="" />);
  fireEvent.click(screen.getByRole('button', { name: /Registrarse/i }));
  fireEvent.change(screen.getByLabelText(/Nombre completo/i), {
    target: { value: 'Dra. Ana' }
  });
  fireEvent.change(screen.getByLabelText(/Correo institucional/i), {
    target: { value: 'ana@redsalud.cl' }
  });
  fireEvent.change(screen.getByLabelText(/^Contrasena$/i), {
    target: { value: 'segura123' }
  });
  fireEvent.change(screen.getByLabelText(/Confirmar/i), {
    target: { value: 'segura123' }
  });
  fireEvent.click(screen.getByRole('button', { name: /Registrar medico/i }));

  await waitFor(() => expect(screen.getByText(/No se pudo registrar/i)).toBeTruthy());
});
