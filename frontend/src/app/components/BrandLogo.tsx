type BrandLogoProps = {
  className?: string;
  tone?: 'light' | 'dark' | 'original';
};

export function BrandLogo({ className = '', tone = 'light' }: BrandLogoProps) {
  const toneClass =
    tone === 'light' ? 'brightness-0 invert' : tone === 'dark' ? 'brightness-0' : '';

  return (
    <img
      alt="Hazel Gym"
      className={`object-contain ${toneClass} ${className}`}
      src="/logo_hazelgym_no_back_1_g.webp"
    />
  );
}
