interface BrandProps {
    size?: 'small' | 'large'
}

function Brand({ size = 'large' }: BrandProps) {
    return (
        <div
            className={`font-bold text-white tracking-tight ${
                size === 'large' ? 'text-3xl' : 'text-xl'
            }`}
        >
            vidoo<span className="text-violet-400">.</span>
        </div>
    )
}

export default Brand