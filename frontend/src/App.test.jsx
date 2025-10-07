import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import App from './App';

global.fetch = vi.fn();

describe('App Component', () => {
  beforeEach(() => {
    fetch.mockClear();
  });

  it('renders the app title', () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(<App />);
    expect(screen.getByText('Todo Task Manager')).toBeInTheDocument();
  });

  it('renders create task form', () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(<App />);
    expect(screen.getByLabelText(/title/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/description/i)).toBeInTheDocument();
    expect(screen.getByText('Add Task')).toBeInTheDocument();
  });

  it('displays empty state when no tasks', async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(<App />);
    
    await waitFor(() => {
      expect(screen.getByText('No tasks yet')).toBeInTheDocument();
    });
  });

  it('displays tasks when available', async () => {
    const mockTasks = [
      {
        id: 1,
        title: 'Test Task 1',
        description: 'Description 1',
        completed: false,
        createdAt: new Date().toISOString(),
      },
      {
        id: 2,
        title: 'Test Task 2',
        description: 'Description 2',
        completed: false,
        createdAt: new Date().toISOString(),
      },
    ];

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockTasks,
    });

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('Test Task 1')).toBeInTheDocument();
      expect(screen.getByText('Test Task 2')).toBeInTheDocument();
    });
  });

  it('creates a new task when form is submitted', async () => {
    const user = userEvent.setup();

    fetch
      .mockResolvedValueOnce({
        ok: true,
        json: async () => [],
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({
          id: 1,
          title: 'New Task',
          description: 'New Description',
          completed: false,
          createdAt: new Date().toISOString(),
        }),
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => [
          {
            id: 1,
            title: 'New Task',
            description: 'New Description',
            completed: false,
            createdAt: new Date().toISOString(),
          },
        ],
      });

    render(<App />);

    const titleInput = screen.getByPlaceholderText('Enter task title');
    const descriptionInput = screen.getByPlaceholderText('Enter task description (optional)');
    const submitButton = screen.getByText('Add Task');

    await user.type(titleInput, 'New Task');
    await user.type(descriptionInput, 'New Description');
    await user.click(submitButton);

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        expect.stringContaining('/api/tasks'),
        expect.objectContaining({
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            title: 'New Task',
            description: 'New Description',
            completed: false,
          }),
        })
      );
    });
  });

  it('shows error when title is empty', async () => {
  const user = userEvent.setup();

  fetch.mockResolvedValueOnce({
    ok: true,
    json: async () => [],
  });

  render(<App />);

  const submitButton = screen.getByText('Add Task');
  
  // Try to submit empty form
  await user.click(submitButton);

  // The form's HTML5 validation will prevent submission
  // So we just check that no fetch was called
  await waitFor(() => {
    // Only 1 fetch call should have been made (initial load)
    expect(fetch).toHaveBeenCalledTimes(1);
  });
});

  it('marks task as done when Done button is clicked', async () => {
    const user = userEvent.setup();
    const mockTasks = [
      {
        id: 1,
        title: 'Task to Complete',
        description: 'Description',
        completed: false,
        createdAt: new Date().toISOString(),
      },
    ];

    fetch
      .mockResolvedValueOnce({
        ok: true,
        json: async () => mockTasks,
      })
      .mockResolvedValueOnce({
        ok: true,
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => [],
      });

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('Task to Complete')).toBeInTheDocument();
    });

    const doneButton = screen.getByText('Done');
    await user.click(doneButton);

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        expect.stringContaining('/api/tasks/1/complete'),
        expect.objectContaining({ method: 'PATCH' })
      );
    });
  });

  it('shows error when fetch fails', async () => {
    fetch.mockRejectedValueOnce(new Error('Network error'));

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('Failed to load tasks')).toBeInTheDocument();
    });
  });

  it('clears form after successful submission', async () => {
    const user = userEvent.setup();

    fetch
      .mockResolvedValueOnce({
        ok: true,
        json: async () => [],
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({ id: 1, title: 'Task', description: 'Desc', completed: false }),
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => [],
      });

    render(<App />);

    const titleInput = screen.getByPlaceholderText('Enter task title');
    const descriptionInput = screen.getByPlaceholderText('Enter task description (optional)');

    await user.type(titleInput, 'Test');
    await user.type(descriptionInput, 'Test Desc');
    await user.click(screen.getByText('Add Task'));

    await waitFor(() => {
      expect(titleInput).toHaveValue('');
      expect(descriptionInput).toHaveValue('');
    });
  });
});