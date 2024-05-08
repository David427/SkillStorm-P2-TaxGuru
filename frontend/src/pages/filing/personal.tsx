import type { User } from "../../types";

import {
  Form,
  Grid,
  Alert,
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
import { states } from "../../states";
import { FormEvent, useState } from "react";
import { valueOrNull } from "../../lib/utils";
import { useTranslation } from "react-i18next";
import { useAuth } from "../../contexts/auth-context";
import { useNavigate, Navigate } from "react-router-dom";

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
  const navigate = useNavigate();

  const { t } = useTranslation();
  const { jwt, user, setUser } = useAuth();

  const [error, setError] = useState<string | null>(null);

  const handlePersonalInfo = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      firstName: e.currentTarget.elements.first_name.value,
      // @ts-expect-error untyped form elements but we need the values
      lastName: e.currentTarget.elements.last_name.value,
      // @ts-expect-error untyped form elements but we need the values
      suffix: valueOrNull(e.currentTarget.elements.suffix.value, t("select")),
      // @ts-expect-error untyped form elements but we need the values
      dateOfBirth: e.currentTarget.elements.birthdate.value,
      // @ts-expect-error untyped form elements but we need the values
      ssn: e.currentTarget.elements.ssn.value,
      // @ts-expect-error untyped form elements but we need the values
      phoneNumber: e.currentTarget.elements.tel.value,
      // @ts-expect-error untyped form elements but we need the values
      streetAddress: e.currentTarget.elements.street_address.value,
      // @ts-expect-error untyped form elements but we need the values
      city: e.currentTarget.elements.city.value,
      // @ts-expect-error untyped form elements but we need the values
      userState: e.currentTarget.elements.state.value,
      // @ts-expect-error untyped form elements but we need the values
      zipCode: e.currentTarget.elements.zipcode.value,
      // these fields aren't included in the form, but we don't want to accidentally override them
      username: user?.username,
      email: user?.email,
      taxReturn: user?.taxReturn,
    };

    const res = await fetch(
      `http://localhost:8080/users/update?username=${user?.username}`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
        },
        body: JSON.stringify(formData),
      }
    );

    if (res.ok) {
      const data: User = await res.json();
      setUser(data);
      navigate("/filing/filing-information");
    } else {
      console.log("Error: ", res.status);
      if (res.headers.get("content-type")?.includes("application/json")) {
        const data = await res.json();
        setError(data.error);
      } else if (res.headers.get("content-type")?.includes("text/plain")) {
        const text = await res.text();
        setError(text);
      }
    }
  };

  if (!jwt) {
    return <Navigate to="/login" />;
  }

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
          <StepIndicatorStep label={t("results.title")} />
        </StepIndicator>

        {error && (
          <Alert type="error" headingLevel="h4" slim>
            {error}
          </Alert>
        )}

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
                  defaultValue={user?.firstName ?? undefined}
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
                  defaultValue={user?.lastName ?? undefined}
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="suffix">{t("personal.suffix")}</Label>
                <Select
                  id="suffix"
                  name="suffix"
                  defaultValue={user?.suffix ?? undefined}
                >
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
                  YYYY-MM-DD
                </span>
                <TextInputMask
                  id="birthdate"
                  name="birthdate"
                  type="text"
                  aria-labelledby="label-birthdate"
                  aria-describedby="hint-birthdate"
                  mask="____-__-__"
                  // the pattern fits the date format YYYY-MM-DD
                  pattern="^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$"
                  defaultValue={user?.dateOfBirth ?? undefined}
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
                  defaultValue={user?.ssn ?? undefined}
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
                  defaultValue={user?.phoneNumber ?? undefined}
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
                  defaultValue={user?.streetAddress ?? undefined}
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
                <TextInput
                  id="city"
                  name="city"
                  type="text"
                  defaultValue={user?.city ?? undefined}
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="state" requiredMarker>
                  {t("personal.state")}
                </Label>
                <Select
                  id="state"
                  name="state"
                  defaultValue={user?.userState ?? undefined}
                  required
                >
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
                  defaultValue={user?.zipCode ?? undefined}
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
