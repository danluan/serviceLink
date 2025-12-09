import { useState, useCallback } from 'react';
import { z } from 'zod';
import { formatZodError, getFieldError } from '@/lib/zod-utils';

export interface UseFormValidationOptions<T> {
  schema: z.ZodSchema<T>;
  onSubmit: (data: T) => Promise<void> | void;
  initialData?: Partial<T>;
}

export const useFormValidationServices = <T extends z.ZodType>(schema: T) => {
    const [errors, setErrors] = useState<Record<string, string>>({});

    const validate = (data: unknown): data is z.infer<T> => {
        try {
            schema.parse(data);
            setErrors({});
            return true;
        } catch (error) {
            if (error instanceof z.ZodError) {
                const newErrors: Record<string, string> = {};
                error.issues.forEach((err) => {
                    if (err.path[0]) {
                        newErrors[err.path[0].toString()] = err.message;
                    }
                });
                setErrors(newErrors);
            }
            return false;
        }
    };

    const clearErrors = () => setErrors({});

    return { errors, validate, clearErrors };
};

export function useFormValidation<T extends Record<string, unknown>>({
  schema,
  onSubmit,
  initialData = {},
}: UseFormValidationOptions<T>) {
  const [data, setData] = useState<Partial<T>>(initialData);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [touched, setTouched] = useState<Record<string, boolean>>({});

  const updateField = useCallback((field: keyof T, value: unknown) => {
    setData((prev) => ({ ...prev, [field]: value }));

    if (errors[field as string]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[field as string];
        return newErrors;
      });
    }
  }, [errors]);

  const setFieldTouched = useCallback((field: keyof T) => {
    setTouched((prev) => ({ ...prev, [field as string]: true }));
  }, []);

  const validateField = useCallback((field: keyof T): boolean => {
    try {
      schema.parse(data);
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[field as string];
        return newErrors;
      });
      return true;
    } catch (error) {
        if (error instanceof z.ZodError) {
            // ASSERÇÃO DE TIPO
            const zodError = error as z.ZodError; // <-- NOVA LINHA

            const formattedErrors = formatZodError(zodError);
            setErrors(formattedErrors);
      }
      return false;
    }
  }, [data, schema]);

  const validate = useCallback((): boolean => {
    try {
      schema.parse(data);
      setErrors({});
      return true;
    } catch (error) {
      if (error instanceof z.ZodError) {
        const formattedErrors = formatZodError(error);
        setErrors(formattedErrors);
      }
      return false;
    }
  }, [data, schema]);

  const handleSubmit = useCallback(
    async (e: React.FormEvent) => {
      e.preventDefault();

      const dataKeys = Object.keys(data);
      const allTouched = dataKeys.reduce((acc, field) => {
        acc[field] = true;
        return acc;
      }, {} as Record<string, boolean>);
      setTouched(allTouched);

      if (!validate()) {
        return;
      }

      setIsSubmitting(true);
      try {
        await onSubmit(data as T);
      } catch (error) {
        console.error('Form submission error:', error);
        throw error;
      } finally {
        setIsSubmitting(false);
      }
    },
    [data, validate, onSubmit]
  );

  const reset = useCallback(() => {
    setData(initialData);
    setErrors({});
    setTouched({});
    setIsSubmitting(false);
  }, [initialData]);

  return {
    data,
    errors,
    isSubmitting,
    touched,
    updateField,
    setFieldTouched,
    validateField,
    handleSubmit,
    validate,
    reset,
  };
}
