import type { ReactNode, ButtonHTMLAttributes } from 'react'

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
    children: ReactNode
    variant?: 'primary' | 'secondary'
    fullWidth?: boolean
}

function Button({
                    children,
                    variant = 'primary',
                    fullWidth = false,
                    className = '',
                    ...props
                }: ButtonProps) {
    const baseStyles =
        'inline-flex items-center justify-center gap-2 rounded-lg px-4 py-3 text-sm font-semibold transition-colors disabled:cursor-not-allowed disabled:opacity-50'

    const variants = {
        primary: 'bg-violet-500 text-white hover:bg-violet-600',
        secondary: 'bg-white text-gray-900 hover:bg-gray-100',
    }

    return (
        <button
            className={`${baseStyles} ${variants[variant]} ${fullWidth ? 'w-full' : ''} ${className}`}
            {...props}
        >
            {children}
        </button>
    )
}

export default Button