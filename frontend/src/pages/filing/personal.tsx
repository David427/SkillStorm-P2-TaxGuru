import {
  Form,
  Grid,
  Label,
  Button,
  Select,
  Fieldset,
  TextInput,
  GridContainer,
  StepIndicator,
  TextInputMask,
  StepIndicatorStep,
} from "@trussworks/react-uswds";
import { FormEvent } from "react";

import { states } from "../../states";

import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";

const suffixOptions = [
  "Jr.",
  "Sr.",
  "II",
  "III",
  "IV",
  "V",
  "Ph.D.",
  "M.D.",
  "J.D.",
  "Esq.",
];

export default function Personal() {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const handlePersonalInfo = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      firstName: e.currentTarget.elements.first_name.value,
      // @ts-expect-error untyped form elements but we need the values
      lastName: e.currentTarget.elements.last_name.value,
      // @ts-expect-error untyped form elements but we need the values
      suffix: e.currentTarget.elements.suffix.value,
      // @ts-expect-error untyped form elements but we need the values
      birthdate: e.currentTarget.elements.birthdate.value,
      // @ts-expect-error untyped form elements but we need the values
      ssn: e.currentTarget.elements.ssn.value,
      // @ts-expect-error untyped form elements but we need the values
      tel: e.currentTarget.elements.tel.value,
      // @ts-expect-error untyped form elements but we need the values
      streetAddress: e.currentTarget.elements.street_address.value,
      // @ts-expect-error untyped form elements but we need the values
      city: e.currentTarget.elements.city.value,
      // @ts-expect-error untyped form elements but we need the values
      state: e.currentTarget.elements.state.value,
      // @ts-expect-error untyped form elements but we need the values
      zipCode: e.currentTarget.elements.zipcode.value,
    };

    console.log(formData);

    navigate("/filing/filing-information");
  };

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label={t("personal.title")} status="current" />
          <StepIndicatorStep label={t("filing-info.title")} />
          <StepIndicatorStep label={t("w2.title")} />
          <StepIndicatorStep label={t("1099.title")} />
          <StepIndicatorStep label={t("deductions.title")} />
          <StepIndicatorStep label={t("review.title")} />
          <StepIndicatorStep label="Results" />
        </StepIndicator>

        <Form onSubmit={handlePersonalInfo} className="w-full">
          <Fieldset legend={t("personal.desc")}>
            {/* FName, LName, Suffix */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="first_name" requiredMarker>
                  {t("personal.fname")}
                </Label>
                <TextInput
                  id="first_name"
                  name="first_name"
                  type="text"
                  autoComplete="given-name"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="last_name" requiredMarker>
                  {t("personal.lname")}
                </Label>
                <TextInput
                  id="last_name"
                  name="last_name"
                  type="text"
                  autoComplete="family-name"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="suffix">{t("personal.suffix")}</Label>
                <Select id="suffix" name="suffix">
                  <option>{t("select")}</option>
                  {suffixOptions.map((s) => (
                    <option key={s} value={s}>
                      {s}
                    </option>
                  ))}
                </Select>
              </Grid>
            </Grid>

            {/* Birthdate and SSN */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label id="label-birthdate" htmlFor="birthdate" requiredMarker>
                  {t("personal.birthdate")}
                </Label>
                <span id="hint-birthdate" className="usa-hint">
                  MM/DD/YYYY
                </span>
                <TextInputMask
                  id="birthdate"
                  name="birthdate"
                  type="text"
                  aria-labelledby="label-birthdate"
                  aria-describedby="hint-birthdate"
                  mask="__/__/____"
                  // the pattern fits the date format MM/DD/YYYY
                  pattern="^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\d{4}$"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label id="label-ssn" htmlFor="ssn" requiredMarker>
                  SSN or TIN
                </Label>
                <span id="hint-ssn" className="usa-hint">
                  123-45-6789
                </span>
                <TextInputMask
                  id="ssn"
                  name="ssn"
                  type="text"
                  aria-labelledby="label-ssn"
                  aria-describedby="hint-ssn"
                  mask="___-__-____"
                  pattern="\d{3}-\d{2}-\d{4}"
                  required
                />
              </Grid>
            </Grid>

            {/* Phone number and Street Address */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label id="label-tel" htmlFor="tel" requiredMarker>
                  {t("personal.tel")}
                </Label>
                <span id="hint-tel" className="usa-hint">
                  123-456-7890
                </span>
                <TextInputMask
                  id="tel"
                  name="tel"
                  type="text"
                  aria-labelledby="label-tel"
                  aria-describedby="hint-tel"
                  mask="___-___-____"
                  pattern="\d{3}-\d{3}-\d{4}"
                  autoComplete="tel-national"
                  required
                />
              </Grid>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="street_address" requiredMarker>
                  {t("personal.street")}
                </Label>
                <span id="hint-street-address" className="usa-hint">
                  123 Park Ln
                </span>
                <TextInput
                  id="street_address"
                  name="street_address"
                  aria-describedby="hint-street-address"
                  type="text"
                  autoComplete="street-address"
                  required
                />
              </Grid>
            </Grid>

            {/* City and State */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="city" requiredMarker>
                  {t("personal.city")}
                </Label>
                <TextInput id="city" name="city" type="text" required />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="state" requiredMarker>
                  {t("personal.state")}
                </Label>
                <Select id="state" name="state" required>
                  <option>{t("select")}</option>
                  {states.map((s) => (
                    <option key={s} value={s}>
                      {s}
                    </option>
                  ))}
                </Select>
              </Grid>
            </Grid>

            {/* Zip and Filing Status */}
            <Grid row gap>
              <Grid tablet={{ col: 6 }}>
                <Label htmlFor="zipcode" requiredMarker>
                  {t("personal.zip")}
                </Label>
                <TextInputMask
                  id="zipcode"
                  name="zipcode"
                  type="text"
                  mask="_____"
                  pattern="^\d{5}"
                  autoComplete="postal-code"
                  required
                />
              </Grid>
            </Grid>

            <Button type="submit">{t("continue")}</Button>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
